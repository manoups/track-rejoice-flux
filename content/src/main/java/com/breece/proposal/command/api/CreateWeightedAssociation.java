package com.breece.proposal.command.api;


import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentInteract;
import com.breece.coreapi.authentication.RequiresRole;
import com.breece.coreapi.authentication.Role;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.command.api.model.WeightedAssociation;
import com.breece.proposal.command.api.model.WeightedAssociationCommand;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@RequiresRole(Role.ADMIN)
public record CreateWeightedAssociation(ContentId contentId, @NotNull SightingId sightingId, WeightedAssociationId weightedAssociationId, @NotNull SightingDetails sightingDetails) implements ContentInteract, WeightedAssociationCommand {


    @AssertLegal
    void assertNew(WeightedAssociation weightedAssociation) {
        throw WeightedProposalErrors.alreadyExists;
    }

    /*@AssertLegal
    void assertCorrectSeeker() {

        Sender sender = Sender.createSender(seeker);
        if(Objects.isNull(sender)) {
            throw new UnauthorizedException("User not found");
        }
        Entity<Content> contentEntity = Fluxzero.loadAggregate(contentId);
        if (contentEntity.isEmpty()) {
            throw ContentErrors.notFound;
        }
        if (sender.nonAuthorizedFor(contentEntity.get().ownerId())) {
            throw LinkedSightingErrors.unauthorized;
        }
    }*/

    @AssertLegal
    void assertCorrectId() {
        if (!weightedAssociationId.getId().equals(contentId+"-"+ sightingId)) {
            throw WeightedProposalErrors.malformedKey;
        }
    }

    @AssertLegal
    void assertSightingExistsAndValid(Sender sender) {
        Sighting sighting = Fluxzero.loadAggregate(sightingId).get();
        if (Objects.isNull(sighting)) {
            throw SightingErrors.notFound;
        }
        if (!sighting.details().equals(sightingDetails)) {
            throw SightingErrors.sightingMismatch;
        }
    }

    @Apply
    WeightedAssociation propose(Sender sender) {
        return new WeightedAssociation(weightedAssociationId, sender.userId(), sightingId, sightingDetails);
    }
}
