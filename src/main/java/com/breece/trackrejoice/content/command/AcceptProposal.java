package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ProposedSighting;
import com.breece.trackrejoice.content.model.ProposedSightingId;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AcceptProposal(@NotNull ProposedSightingId proposedSightingId,
                             @NotNull @Valid SightingDetails sightingDetails,
                             @NotNull ContentId contentId) implements ContentUpdate {

    /*@AssertLegal
    void assertLinked() {
        Content content = Fluxzero.search(Content.class)
                .match(contentId, "contentId")
                .fetchFirstOrNull();
        if (content == null) {
            throw new IllegalArgumentException("Content not found");
        }
        if (content.proposedSightings().stream().noneMatch(ps -> ps.proposedSightingId().equals(proposedSightingId))) {
            throw new IllegalArgumentException("Sighting not proposed for content");
        }
    }*/

    @AssertLegal
    void assertSightingExists(Content content) {
        if (content.proposedSightings().stream().map(ProposedSighting::proposedSightingId).noneMatch(p -> p.equals(proposedSightingId))) {
            throw new IllegalArgumentException("Sighting not proposed for content");
        }
    }

    @AssertLegal
    void assertDataMatch(Content content) {
        content.proposedSightings().stream().filter(p -> p.proposedSightingId().equals(proposedSightingId)).findFirst().ifPresent(
                proposedSighting -> {
                    if (!proposedSighting.details().equals(sightingDetails)) {
                        throw new IllegalArgumentException("Incorrect details provided");
                    }
                }
        );
    }

    @Apply
    Content apply(Content content) {
        return content.withLastConfirmedSighting(sightingDetails);
    }
}
