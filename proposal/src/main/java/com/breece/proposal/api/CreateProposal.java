package com.breece.proposal.api;


import com.breece.content.ContentErrors;
import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import com.breece.proposal.api.model.LinkedSighting;
import com.breece.proposal.api.model.LinkedSightingCommand;
import com.breece.proposal.api.model.LinkedSightingId;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public record CreateProposal(@NotNull ContentId contentId, @NotNull UserId seeker, @NotNull SightingId sourceSightingId, @NotNull LinkedSightingId linkedSightingId, @NotNull SightingDetails sightingDetails) implements LinkedSightingCommand {
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
        Entity<Content> contentEntity = Fluxzero.loadAggregate(contentId);
        if (contentEntity.isEmpty()) {
            throw ContentErrors.notFound;
        }
        if (sender.nonAuthorizedFor(contentEntity.get().ownerId())) {
            throw LinkedSightingErrors.unauthorized;
        }
    }

    @AssertLegal
    void assertCorrectId() {
        if (!linkedSightingId.getId().equals(contentId+"-"+sourceSightingId)) {
            throw LinkedSightingErrors.malformedKey;
        }
    }

    @AssertLegal
    void assertSightingExistsAndValid(Sender sender) {
        Sighting sighting = Fluxzero.loadAggregate(sourceSightingId).get();
        if (Objects.isNull(sighting)) {
            throw SightingErrors.notFound;
        }
        if (!sighting.details().equals(sightingDetails)) {
            throw SightingErrors.sightingMismatch;
        }
    }

    @Apply
    LinkedSighting propose(Sender sender) {
        return new LinkedSighting(linkedSightingId, sender.userId(), seeker, sourceSightingId, contentId, sightingDetails);
    }
}
