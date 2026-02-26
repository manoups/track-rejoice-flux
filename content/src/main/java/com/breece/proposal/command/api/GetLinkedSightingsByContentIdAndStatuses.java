package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.command.api.model.WeightedAssociation;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationState;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


public record GetLinkedSightingsByContentIdAndStatuses(@NotNull ContentId contentId, @NotNull @NotEmpty List<WeightedAssociationStatus> statuses) implements Request<List<WeightedAssociation>> {
    @HandleQuery
    List<WeightedAssociation> find(Sender sender) {
        Optional<Content> content = Fluxzero.search(Content.class)
                .match(contentId, "contentId")
                .match(sender.isAdmin() ? null : sender.userId(), "ownerId")
//                .any(ids.stream().map(v -> match(v, "weightedAssociationId")).toArray(Constraint[]::new))
                .fetchFirst();
        Optional<List<WeightedAssociationId>> weightedAssociationIds = content
                .map(Content::weightedAssociations).map(l -> l.stream().map(WeightedAssociation::weightedAssociationId).toList());
        if (weightedAssociationIds.isEmpty() || weightedAssociationIds.get().isEmpty()) {
            return Collections.emptyList();
        }
        List<WeightedAssociationState> weightedAssociationStates = Fluxzero.queryAndWait(new GetLinkedSightingStates(weightedAssociationIds.get(), statuses));
        return content.get().weightedAssociations().stream().filter(l -> weightedAssociationStates.stream().anyMatch(s -> s.weightedAssociationId().equals(l.weightedAssociationId()))).toList();
    }

}
