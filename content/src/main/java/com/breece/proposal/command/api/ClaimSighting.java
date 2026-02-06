package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentInteract;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.command.api.model.LinkedSightingCommand;
import com.breece.proposal.command.api.model.LinkedSightingId;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.persisting.eventsourcing.InterceptApply;
import jakarta.validation.constraints.NotNull;

public record ClaimSighting(@NotNull ContentId contentId, @NotNull SightingId sightingId, @NotNull SightingDetails sightingDetails, @NotNull LinkedSightingId linkedSightingId) implements ContentInteract {

    @InterceptApply
    LinkedSightingCommand interceptApply(Content content, Sender sender) {
        if (content.linkedSightings().stream().anyMatch(ls -> ls.linkedSightingId().equals(linkedSightingId))) {
            return new AcceptProposal(contentId, linkedSightingId);
        }
        return new CreateProposal(contentId, sightingId, linkedSightingId, sightingDetails);
    }
}
