package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.command.api.model.WeightedAssociationState;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.common.api.search.Constraint;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.List;

import static io.fluxzero.common.api.search.constraints.FacetConstraint.matchFacet;


public record GetWeightedAssociationsBySightingIdAndStatuses(@NotNull SightingId sightingId,
                                                             @NotNull @NotEmpty List<WeightedAssociationStatus> statuses) implements Request<List<WeightedAssociationState>> {
    @HandleQuery
    List<WeightedAssociationState> find(Sender sender) {
//        Ownership check
        if (Fluxzero.search(Sighting.class)
                .match(sightingId, "sightingId")
                .match(sender.isAdmin() ? null : sender.userId(), "ownerId")
                .fetchFirst().isEmpty()) {
            return Collections.emptyList();
        }
        return Fluxzero.search(WeightedAssociationState.class)
                .match(sightingId, "sightingId")
                .any(statuses.stream().map(v -> matchFacet("status", v)).toArray(Constraint[]::new))
                .fetchAll();
    }

}
