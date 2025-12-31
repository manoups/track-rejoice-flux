package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ProposedSighting;
import com.breece.trackrejoice.content.model.ProposedSightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record AcceptProposal(@NotNull ProposedSightingId proposedSightingId, @NotNull ContentId contentId) implements ContentUpdate {

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

    @Apply
    Content apply(Content content) {
        ProposedSighting sighting = Fluxzero.loadEntity(proposedSightingId).get();
        return content.withLastConfirmedSighting(sighting.details());
    }
}
