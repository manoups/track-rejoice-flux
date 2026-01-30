package com.breece.proposal.api;

import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.api.model.LinkedSighting;
import com.breece.proposal.api.model.LinkedSightingStatus;
import io.fluxzero.common.api.search.Constraint;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import static io.fluxzero.common.api.search.constraints.MatchConstraint.match;


public record GetLinkedSightingsByContentIdAndStatuses(@NotNull ContentId contentId, @NotNull @NotEmpty List<LinkedSightingStatus> statuses) implements Request<List<LinkedSighting>> {
    @HandleQuery
    List<LinkedSighting> find(Sender sender) {
        return Fluxzero.search(LinkedSighting.class)
                .match(contentId, "contentId")
                .match(sender.isAdmin() ? null : sender.userId(), "seeker")
                .any(statuses.stream().map(v -> match(v, "orderId")).toArray(Constraint[]::new))
                .fetchAll();
    }

}
