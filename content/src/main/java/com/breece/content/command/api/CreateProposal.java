package com.breece.content.command.api;


import com.breece.common.model.Content;
import com.breece.common.model.ContentId;
import com.breece.common.model.ProposedSighting;
import com.breece.common.model.ProposedSightingId;
import com.breece.common.sighting.SightingContentBridge;
import com.breece.common.sighting.SightingErrors;
import com.breece.common.sighting.model.Sighting;
import com.breece.common.sighting.model.SightingDetails;
import com.breece.common.sighting.model.SightingId;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record CreateProposal(ContentId contentId, @NotNull SightingId sightingId, @NotNull ProposedSightingId proposedSightingId, @NotNull SightingDetails sightingDetails, boolean removeAfterMatching) implements ContentInteract, SightingContentBridge {
    @AssertLegal
    void assertSightingExists() {
        if (!Fluxzero.loadAggregate(sightingId).isPresent()) {
            throw SightingErrors.notFound;
        }
    }

    @AssertLegal
    void assertOwner(Sender sender) {
        Sighting sighting = Fluxzero.loadAggregate(sightingId).get();
        if (!sender.isAuthorizedFor(sighting.source())) {
            throw SightingErrors.notOwner;
        }
    }

    @AssertLegal
    void assertPreferenceConsistency() {
        Sighting sighting = Fluxzero.loadAggregate(sightingId).get();
        if (sighting.removeAfterMatching() != removeAfterMatching) {
            throw SightingErrors.sightingMismatch;
        }
    }

    @AssertLegal
    void assertUnique(Content content) {
        if (content.proposedSightings().stream().anyMatch(p -> p.sightingId().equals(sightingId))) {
            throw SightingErrors.alreadyProposed;
        }
    }

    @AssertLegal
    void asserNotLastSeenLocation(Content content){
        if (sightingDetails.equals(content.lastConfirmedSighting())) {
            throw SightingErrors.alreadyProposed;
        }
    }

    @Apply
    ProposedSighting propose() {
        return new ProposedSighting(proposedSightingId, sightingId, sightingDetails, removeAfterMatching);
    }
}
