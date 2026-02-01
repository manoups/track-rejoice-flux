package com.breece.proposal.api;


import com.breece.content.ContentErrors;
import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.query.api.GetContent;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import com.breece.proposal.api.model.LinkedSighting;
import com.breece.proposal.api.model.LinkedSightingCommand;
import com.breece.proposal.api.model.LinkedSightingId;
import com.breece.proposal.api.model.LinkedSightingStatus;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.query.api.GetSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

import static io.fluxzero.sdk.common.Message.asMessage;

public record CreateProposal(@NotNull ContentId contentId, @NotNull UserId seeker, @NotNull SightingId sightingId, @NotNull LinkedSightingId linkedSightingId, @NotNull SightingDetails sightingDetails, boolean removeAfterMatching) implements
        LinkedSightingCommand, SightingContentBridge {
    /*@AssertLegal
    void assertSightingExists() {
        if (!Fluxzero.loadAggregate(sightingId).isPresent()) {
            throw SightingErrors.notFound;
        }
    }*/

    @AssertLegal
    void assertNew(LinkedSighting linkedSighting) {
        throw LinkedSightingErrors.alreadyExists;
    }

    @AssertLegal
    void assertCorrectSeeker() {
        Sender sender = Sender.createSender(seeker);
        if(Objects.isNull(sender)) {
            throw new UnauthorizedException("User not found");
        }
        Content content = Fluxzero.queryAndWait(asMessage(new GetContent(contentId)).addUser(sender));
        if (Objects.isNull(content)) {
            throw ContentErrors.notFound;
        }

        if (sender.nonAuthorizedFor(content.ownerId())) {
            throw LinkedSightingErrors.unauthorized;
        }
    }

    @AssertLegal
    void assertCorrectId() {
        if (!linkedSightingId.getId().equals(contentId+"-"+sightingId)) {
            throw LinkedSightingErrors.malformedKey;
        }
    }

    @AssertLegal
    void assertSightingExistsAndValid(Sender sender) {
        Sighting sighting = Fluxzero.queryAndWait(asMessage(new GetSighting(sightingId)));
        if (Objects.isNull(sighting)) {
            throw SightingErrors.notFound;
        }
        if (!sighting.details().equals(sightingDetails)) {
            throw SightingErrors.sightingMismatch;
        }
        if (removeAfterMatching != sighting.removeAfterMatching()) {
            throw SightingErrors.sightingMismatch;
        }
    }

    @Apply
    LinkedSighting propose(Sender sender) {
        return new LinkedSighting(linkedSightingId, sender.userId(), seeker, sightingId, contentId, sightingDetails, LinkedSightingStatus.CREATED);
    }
}
