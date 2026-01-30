package com.breece.proposal.api;


import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.api.model.LinkedSightingCommand;
import com.breece.proposal.api.model.LinkedSightingId;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.persisting.eventsourcing.InterceptApply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record ClaimSighting(@NotNull ContentId contentId, @NotNull SightingId sightingId, @NotNull SightingDetails sightingDetails, boolean removeAfterMatching, LinkedSightingId linkedSightingId) implements LinkedSightingCommand, SightingContentBridge {

    @InterceptApply
    CreateProposal interceptApply(Sender sender) {
        return new CreateProposal(contentId, sender.userId(), sightingId, sender.userId(), linkedSightingId, sightingDetails, removeAfterMatching);
    }
}
