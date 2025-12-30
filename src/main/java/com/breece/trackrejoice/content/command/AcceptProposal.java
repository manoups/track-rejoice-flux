package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.api.SightingUpdate;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record AcceptProposal(@NotNull SightingId sightingId, @NotNull ContentId contentId) implements SightingUpdate {

    @AssertLegal
    void assertLinked() {
        Content content = Fluxzero.search(Content.class)
                .match(contentId, "contentId")
                .fetchFirstOrNull();
        if (content == null) {
            throw new IllegalArgumentException("Content not found");
        }
        if (content.details().getProposedSightings().stream().noneMatch(ps -> ps.sightingId().equals(sightingId))) {
            throw new IllegalArgumentException("Sighting not proposed for content");
        }
    }

    @Apply
    Sighting apply(Sighting sighting) {
        Fluxzero.sendAndForgetCommand(new ClaimSighting(contentId, sightingId));
        return sighting;
    }
}
