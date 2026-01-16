package com.breece.content.command.api;

import com.breece.common.model.Content;
import com.breece.common.model.ContentId;
import com.breece.common.model.ProposedSighting;
import com.breece.common.model.ProposedSightingId;
import com.breece.common.sighting.ConfirmedSightingUpdate;
import com.breece.common.sighting.SightingErrors;
import com.breece.common.sighting.model.SightingDetails;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AcceptProposal(@NotNull ProposedSightingId proposedSightingId,
                             @NotNull @Valid SightingDetails sightingDetails,
                             @NotNull ContentId contentId) implements ContentUpdate, ConfirmedSightingUpdate {
    @AssertLegal(priority = -10)
    void assertProposedSightingExists(Content content) {
        if (content.proposedSightings().stream().map(ProposedSighting::proposedSightingId).noneMatch(p -> p.equals(proposedSightingId))) {
            throw SightingErrors.notLinkedToContent;
        }
    }

    @AssertLegal
    void assertDataMatch(Content content) {
        content.proposedSightings().stream().filter(p -> p.proposedSightingId().equals(proposedSightingId)).findFirst().ifPresent(
                proposedSighting -> {
                    if (!proposedSighting.details().equals(sightingDetails)) {
                        throw SightingErrors.sightingMismatch;
                    }
                }
        );
    }

    @Apply
    Content apply(Content content) {
        return content.withLastConfirmedSighting(sightingDetails);
    }
}
