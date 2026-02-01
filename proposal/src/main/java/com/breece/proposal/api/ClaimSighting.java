package com.breece.proposal.api;

import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.api.model.LinkedSighting;
import com.breece.proposal.api.model.LinkedSightingCommand;
import com.breece.proposal.api.model.LinkedSightingId;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.persisting.eventsourcing.InterceptApply;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public record ClaimSighting(@NotNull ContentId contentId, @NotNull SightingId sightingId, @NotNull SightingDetails sightingDetails, boolean removeAfterMatching, @NotNull LinkedSightingId linkedSightingId) implements LinkedSightingCommand, SightingContentBridge {

    @InterceptApply
    LinkedSightingCommand interceptApply(@Nullable LinkedSighting linkedSighting, Sender sender) {
        if (Objects.nonNull(linkedSighting)) {
            return new AcceptProposal(linkedSighting.linkedSightingId());
        }
        return new CreateProposal(contentId, sender.userId(), sightingId, linkedSightingId, sightingDetails, removeAfterMatching);
    }
}
