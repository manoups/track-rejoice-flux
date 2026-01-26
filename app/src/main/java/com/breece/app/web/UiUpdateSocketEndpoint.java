package com.breece.app.web;

import com.breece.app.web.api.UiUpdate;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.user.WithOwner;
import com.breece.coreapi.user.api.UserId;
import com.fasterxml.jackson.databind.JsonNode;
import com.flipkart.zjsonpatch.JsonDiff;
import io.fluxzero.common.serialization.JsonUtils;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.common.Message;
import io.fluxzero.sdk.common.serialization.Serializer;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.tracking.handling.HandleNotification;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import io.fluxzero.sdk.web.HandleSocketClose;
import io.fluxzero.sdk.web.HandleSocketOpen;
import io.fluxzero.sdk.web.SocketEndpoint;
import io.fluxzero.sdk.web.SocketSession;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@SocketEndpoint
@Slf4j
public class UiUpdateSocketEndpoint {
    private UserId userId;
    private SocketSession session;

    @HandleSocketOpen("/api/updates")
    @RequiresUser
    void startListening(Sender user, SocketSession session) {
        userId = user.userId();
        this.session = session;
    }

    @HandleSocketClose("/api/updates")
    void stopListening(SocketSession session) {
        session.close();
    }

    @HandleNotification
    void handleUserUpdate(Entity<WithOwner> entity) {
        handleAnyUpdate(entity);
    }

    <T extends WithOwner> void handleAnyUpdate(Entity<T> entity) {
        handleAnyUpdate(entity.id().toString(), entity.previous().get(), entity.get(), entity.lastEventIndex(),
                UiUpdate.Type.valueOf(entity.type().getSimpleName()));
    }

    <T extends WithOwner> void handleAnyUpdate(String entityId, T before, T after, Long index, UiUpdate.Type type) {
        Serializer serializer = Fluxzero.get().serializer();
        try {
            var sender = Sender.createSender(userId);
            if (sender == null) {
                log.info("User {} not found. Closing socket sessions.", userId);
                session.close();
                return;
            }
            UserId ownerId = Objects.isNull(before) ? after.ownerId() : before.ownerId();
            if (!sender.isAuthorizedFor(ownerId)) {
                return;
            }
            var b = serializer.filterContent(before, sender);
            var a = serializer.filterContent(after, sender);
            if (a != null || b != null) {
                try {
                    JsonNode source = JsonUtils.convertValue(b, JsonNode.class);
                    JsonNode target = JsonUtils.convertValue(a, JsonNode.class);
                    JsonNode patch = JsonDiff.asJson(source, target);
                    if (!patch.isEmpty()) {
                        UiUpdate update = new UiUpdate(type, index, entityId, patch);
                        session.sendMessage(Message.asMessage(update)
                                .addMetadata("subscriber", userId.getFunctionalId()));
                    }
                } catch (Throwable e) {
                    log.warn("Failed to send update to user {} (session: {})", userId, session.sessionId(), e);
                }
            }
        } catch (Throwable e) {
            log.error("Failed to send update to ui (userId: {})", userId, e);
        }
    }
}
