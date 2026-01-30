package com.breece.proposal.api;


import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import com.breece.proposal.api.model.LinkedSighting;
import com.breece.proposal.api.model.LinkedSightingCommand;
import com.breece.proposal.api.model.LinkedSightingId;
import com.breece.proposal.api.model.LinkedSightingStatus;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record CreateProposal(@NotNull ContentId contentId, @NotNull UserId targetUser, @NotNull SightingId sightingId, @NotNull UserId sourceUser, @NotNull LinkedSightingId linkedSightingId, @NotNull SightingDetails sightingDetails, boolean removeAfterMatching) implements
        LinkedSightingCommand, SightingContentBridge {
    @AssertLegal
    void assertSightingExists() {
        if (!Fluxzero.loadAggregate(sightingId).isPresent()) {
            throw SightingErrors.notFound;
        }
    }

    @AssertLegal
    void assertNew(LinkedSighting linkedSighting) {
        throw LinkedSightingErrors.alreadyExists;
    }

    @Apply
    LinkedSighting propose(Sender sender) {
        return new LinkedSighting(new LinkedSightingId(contentId, sightingId), sender.userId(), targetUser, sightingId, contentId, sightingDetails, LinkedSightingStatus.CREATED);
    }
}
