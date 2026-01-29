package com.breece.content.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.LinkedSighting;
import com.breece.content.api.model.LinkedSightingId;
import com.breece.content.api.ConfirmedSightingUpdate;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.SightingDetails;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AcceptProposal(@NotNull LinkedSightingId linkedSightingId,
                             @NotNull @Valid SightingDetails sightingDetails,
                             @NotNull ContentId contentId,
                             @NotNull SightingId sightingId) implements ActiveContentUpdate, ConfirmedSightingUpdate {
    @AssertLegal(priority = -10)
    void assertLinkedSightingExists(Content content) {
        if (content.linkedSightings().stream().map(LinkedSighting::linkedSightingId).noneMatch(p -> p.equals(linkedSightingId))) {
            throw SightingErrors.notLinkedToContent;
        }
    }

    @AssertLegal
    void assertDataMatch(Content content) {
        content.linkedSightings().stream().filter(p -> p.linkedSightingId().equals(linkedSightingId)).findFirst().ifPresent(
                proposedSighting -> {
                    if (!proposedSighting.details().equals(sightingDetails)) {
                        throw SightingErrors.sightingMismatch;
                    }
                }
        );
    }

    @AssertLegal
    void assertSightingMatch(Content content) {
        content.linkedSightings().stream().filter(p -> p.linkedSightingId().equals(linkedSightingId)).findFirst().ifPresent(
                proposedSighting -> {
                    if (!proposedSighting.sightingId().equals(sightingId)) {
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
