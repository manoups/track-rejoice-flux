package com.breece.proposal.api;

import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.api.model.LinkedSighting;
import com.breece.proposal.api.model.LinkedSightingId;
import com.breece.proposal.api.model.LinkedSightingStatus;
import com.breece.proposal.api.model.LinkedSightingUpdate;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record AcceptProposal(@NotNull LinkedSightingId linkedSightingId,
                             @NotNull @Valid SightingDetails sightingDetails,
                             @NotNull ContentId contentId,
                             @NotNull SightingId sightingId) implements LinkedSightingUpdate {

    @AssertLegal
    void assertPermitted(LinkedSighting linkedSighting, Sender sender) {
        if (sender.nonAuthorizedFor(linkedSighting.seeker())) {
            throw LinkedSightingErrors.unauthorized;
        }
    }

    @Apply
    LinkedSighting apply(LinkedSighting linkedSighting) {
        return linkedSighting.withStatus(LinkedSightingStatus.ACCEPTED);
    }
}
