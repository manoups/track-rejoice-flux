package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ProposedSighting;
import com.breece.trackrejoice.content.model.ProposedSightingId;
import com.breece.trackrejoice.sighting.SightingErrors;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AcceptProposal(@NotNull ProposedSightingId proposedSightingId,
                             @NotNull @Valid SightingDetails sightingDetails,
                             @NotNull SightingId sightingId,
                             @NotNull ContentId contentId) implements ContentUpdate {

    @AssertLegal
    void assertSightingIdMatch(Content content) {
        content.proposedSightings().stream().filter(p -> p.proposedSightingId().equals(proposedSightingId)).findFirst().ifPresent(
                proposedSighting -> {
                    if (!proposedSighting.sightingId().equals(sightingId)) {
                        throw new IllegalArgumentException("Incorrect Signing ID provided");
                    }
                });
    }

    @AssertLegal(priority = -10)
    void assertProposedSightingExists(Content content) {
        if (content.proposedSightings().stream().map(ProposedSighting::proposedSightingId).noneMatch(p -> p.equals(proposedSightingId))) {
            throw SightingErrors.notLinkedToContent;
        }
    }

    @AssertLegal
    void assertSightingStillValid(Content content) {
        if (Fluxzero.loadAggregate(sightingId).isEmpty()) {
            throw SightingErrors.notFound;
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
