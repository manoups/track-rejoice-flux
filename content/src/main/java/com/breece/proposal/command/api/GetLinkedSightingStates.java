package com.breece.proposal.command.api;

import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.LinkedSightingState;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import io.fluxzero.common.api.search.Constraint;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import static io.fluxzero.common.api.search.constraints.MatchConstraint.match;

public record GetLinkedSightingStates(@NotNull @NotEmpty List<WeightedAssociationId> ids, @NotNull @NotEmpty List<WeightedAssociationStatus> statuses) implements Request<List<LinkedSightingState>> {
    @HandleQuery
    List<LinkedSightingState> find() {
        return Fluxzero.search(LinkedSightingState.class)
                .any(ids.stream().map(v -> match(v, "linkedSightingId")).toArray(Constraint[]::new))
                .any(statuses.stream().map(v -> match(v, "status")).toArray(Constraint[]::new))
                .fetchAll();
    }
}
