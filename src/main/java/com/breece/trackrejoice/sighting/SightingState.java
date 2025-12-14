package com.breece.trackrejoice.sighting;

import com.breece.trackrejoice.sighting.api.ClaimSighting;
import com.breece.trackrejoice.sighting.api.CreateSighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.Stateful;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Stateful
@Consumer(name = "sighting-state", ignoreSegment = true)
@AllArgsConstructor
@Getter
public class SightingState {
    @EntityId
    @Association
    private final SightingId sightingId;
    boolean claimed;

    @HandleEvent
    static SightingState on(CreateSighting event) {
        return new SightingState(event.sightingId(), false);
    }

    @HandleEvent
    SightingState on(ClaimSighting event) {
        claimed = true;
        return this;
    }
}
