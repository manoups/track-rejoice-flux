package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.WeightedAssociation;
import com.breece.content.command.api.ContentUpdate;
import com.breece.coreapi.authentication.RequiresRole;
import com.breece.coreapi.authentication.Role;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RequiresRole(Role.ADMIN)
public record CreateWeightedAssociation(ContentId contentId, @NotNull WeightedAssociationId weightedAssociationId, @NotNull SightingId sightingId,
                                        @NotNull @Valid SightingDetails sightingDetails) implements ContentUpdate {
    @AssertLegal
    public void assertNew(WeightedAssociation weightedAssociation) {
        throw WeightedAssociationErrors.alreadyExists;
    }

    @AssertLegal
    public void assertNewSighting(Content content) {
        if(content.weightedAssociations().stream().map(WeightedAssociation::sightingId).anyMatch(sightingId::equals)) {
            throw WeightedAssociationErrors.alreadyExists;
        }
    }

    @Apply
    public WeightedAssociation apply(Content content) {
        return new WeightedAssociation(weightedAssociationId, sightingId, sightingDetails, WeightedAssociationStatus.CREATED);
    }
}
