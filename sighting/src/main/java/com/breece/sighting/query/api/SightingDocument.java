package com.breece.sighting.query.api;

import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.persisting.search.SearchHit;
import lombok.Builder;

import java.time.Instant;

@Builder
public record SightingDocument(SightingId sightingId, UserId ownerId, SightingDetails details, boolean removeAfterMatching, Instant timestamp) {
    public SightingDocument(SearchHit<Sighting> searchHit) {
        this(searchHit.getValue(), searchHit.getTimestamp());
    }

    public SightingDocument(Sighting sighting, Instant timestamp) {
        this(sighting.sightingId(), sighting.ownerId(), sighting.details(), sighting.removeAfterMatching(), timestamp);
    }
}
