package com.breece.trackrejoice.sighting;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.sighting.api.ClaimSighting;
import com.breece.trackrejoice.sighting.api.CreateSighting;
import com.breece.trackrejoice.sighting.api.PostSighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Entity;
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
        Fluxzero.publishEvent(new PostSighting(event.sightingId(), event.sightingDetails()));
        return new SightingState(event.sightingId(), false);
    }

    @HandleEvent
    SightingState on(ClaimSighting event) {
        claimed = true;
        Content contentEntity = Fluxzero.loadAggregate(event.contentId()).get();
        Fluxzero.publishEvent(new LinkSightingToContent(contentEntity.contentId(), contentEntity.details(), event.sightingId()));
        return this;
    }
}
