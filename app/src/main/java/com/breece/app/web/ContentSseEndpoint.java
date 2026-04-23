package com.breece.app.web;

import com.breece.app.web.api.SseEventData;
import com.breece.app.web.api.SseEventData.ChangeType;
import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.command.api.model.WeightedAssociationState;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.tracking.handling.HandleNotification;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;
import io.fluxzero.sdk.web.DefaultWebRequestContext;
import io.fluxzero.sdk.web.HandleGet;
import io.fluxzero.sdk.web.Path;
import io.fluxzero.sdk.web.PathParam;
import io.jooby.ServerSentEmitter;
import io.jooby.ServerSentMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Path("/api/content")
@Slf4j
public class ContentSseEndpoint {

    private final Map<String, Set<ServerSentEmitter>> activeConnections = new ConcurrentHashMap<>();
    private final Map<ServerSentEmitter, AtomicLong> emitterEventIds = new ConcurrentHashMap<>();

    @HandleGet("{contentId}/proposals/stream")
    void stream(@PathParam ContentId contentId, Sender sender) {
        // Validate content exists and user is authorized
        Entity<Content> contentEntity = Fluxzero.loadAggregate(contentId);
        if (!contentEntity.isPresent()) {
            throw new IllegalCommandException("Content not found");
        }
        Content content = contentEntity.get();
        if (sender.nonAuthorizedFor(content.ownerId())) {
            throw new UnauthorizedException("Unauthorized for action");
        }

        String contentIdStr = contentId.toString();

        DefaultWebRequestContext context = DefaultWebRequestContext.getCurrentWebRequestContext();
        context.upgrade((ServerSentEmitter sse) -> {
            AtomicLong eventId = new AtomicLong(0);
            emitterEventIds.put(sse, eventId);
            activeConnections.computeIfAbsent(contentIdStr, k -> ConcurrentHashMap.newKeySet()).add(sse);

            sse.onClose(() -> removeEmitter(contentIdStr, sse));

            // Send initial snapshot
            List<WeightedAssociationState> weightedAssociations = Fluxzero.search(WeightedAssociationState.class)
                    .match(contentId, "contentId")
                    .fetchAll();

            List<SseEventData> snapshotEvents = buildSnapshotEvents(weightedAssociations);
            for (SseEventData eventData : snapshotEvents) {
                ServerSentMessage message = new ServerSentMessage(eventData);
                message.setId(String.valueOf(eventId.incrementAndGet()));
                message.setEvent("snapshot");
                sse.send(message);
            }
        });
    }

    @HandleNotification
    void handleWeightedAssociationUpdate(Entity<WeightedAssociationState> entity) {
        WeightedAssociationState current = entity.get();
        WeightedAssociationState previous = entity.previous().get();

        // Determine contentId from current or previous state
        ContentId contentId = current != null ? current.contentId() : previous != null ? previous.contentId() : null;
        if (contentId == null) {
            return;
        }

        String contentIdStr = contentId.toString();
        Set<ServerSentEmitter> emitters = activeConnections.get(contentIdStr);
        if (emitters == null || emitters.isEmpty()) {
            return;
        }

        SseEventData eventData = buildUpdateEvent(current, previous);

        for (ServerSentEmitter sse : emitters) {
            try {
                if (sse.isOpen()) {
                    AtomicLong eventId = emitterEventIds.get(sse);
                    if (eventId != null) {
                        ServerSentMessage message = new ServerSentMessage(eventData);
                        message.setId(String.valueOf(eventId.incrementAndGet()));
                        message.setEvent("update");
                        sse.send(message);
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to send update to emitter for content {}: {}", contentIdStr, e.getMessage());
                removeEmitter(contentIdStr, sse);
            }
        }
    }

    public static List<SseEventData> buildSnapshotEvents(List<WeightedAssociationState> records) {
        return records.stream()
                .map(state -> new SseEventData(ChangeType.INITIAL, state))
                .toList();
    }

    public static SseEventData buildUpdateEvent(WeightedAssociationState current, WeightedAssociationState previous) {
        ChangeType changeType;
        Object payload;
        if (current == null) {
            changeType = ChangeType.DELETED;
            payload = Map.of("weightedAssociationId", previous.weightedAssociationId().toString());
        } else if (previous == null) {
            changeType = ChangeType.CREATED;
            payload = current;
        } else {
            changeType = ChangeType.UPDATED;
            payload = current;
        }
        return new SseEventData(changeType, payload);
    }

    private void removeEmitter(String contentIdStr, ServerSentEmitter sse) {
        emitterEventIds.remove(sse);
        Set<ServerSentEmitter> emitters = activeConnections.get(contentIdStr);
        if (emitters != null) {
            emitters.remove(sse);
            if (emitters.isEmpty()) {
                activeConnections.remove(contentIdStr);
            }
        }
    }
}
