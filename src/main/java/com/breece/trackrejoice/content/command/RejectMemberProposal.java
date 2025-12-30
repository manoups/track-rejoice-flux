package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RejectMemberProposal(@NotNull SightingId sightingId, @NotNull ContentId contentId) implements ContentUpdate {
    @Apply
    Content apply(Content content) {
        Sighting sighting = Fluxzero.loadEntity(sightingId).get();
        List<Sighting> sightings = content.proposedSightings();
        sightings.remove(sighting);
        return content.withProposedSightings(sightings);
    }
}
