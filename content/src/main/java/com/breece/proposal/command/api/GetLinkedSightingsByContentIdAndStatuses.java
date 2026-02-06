package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.command.api.model.LinkedSighting;
import com.breece.proposal.command.api.model.LinkedSightingId;
import com.breece.proposal.command.api.model.LinkedSightingState;
import com.breece.proposal.command.api.model.LinkedSightingStatus;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


public record GetLinkedSightingsByContentIdAndStatuses(@NotNull ContentId contentId, @NotNull @NotEmpty List<LinkedSightingStatus> statuses) implements Request<List<LinkedSighting>> {
    @HandleQuery
    List<LinkedSighting> find(Sender sender) {
        Optional<Content> content = Fluxzero.search(Content.class)
                .match(contentId, "contentId")
                .match(sender.isAdmin() ? null : sender.userId(), "ownerId")
//                .any(ids.stream().map(v -> match(v, "linkedSightingId")).toArray(Constraint[]::new))
                .fetchFirst();
        Optional<List<LinkedSightingId>> linkedSightingIds = content
                .map(Content::linkedSightings).map(l -> l.stream().map(LinkedSighting::linkedSightingId).toList());
        if (linkedSightingIds.isEmpty() || linkedSightingIds.get().isEmpty()) {
            return Collections.emptyList();
        }
        List<LinkedSightingState> linkedSightingStates = Fluxzero.queryAndWait(new GetLinkedSightingStates(linkedSightingIds.get(), statuses));
        return content.get().linkedSightings().stream().filter(l -> linkedSightingStates.stream().anyMatch(s -> s.linkedSightingId().equals(l.linkedSightingId()))).toList();
    }

}
