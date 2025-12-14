package com.breece.trackrejoice.sighting;

import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Geometry;

@RequiresUser
public record CreateSighting(@NotNull SightingId sightingId, Geometry spottedLocation) implements SightingCommand {
    @AssertLegal
    void assertNew(Sighting sighting) {
        throw SightingErrors.alreadyExists;
    }

    @Apply
    Sighting create() {
        return new Sighting(sightingId, new SightingDetails(spottedLocation), false);
    }
}
