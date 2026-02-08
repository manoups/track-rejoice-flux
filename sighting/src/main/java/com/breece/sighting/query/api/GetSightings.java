package com.breece.sighting.query.api;


import com.breece.sighting.api.model.Sighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public record GetSightings(@PositiveOrZero Integer page, @Positive Integer pageSize,
                           String filter) implements Request<List<SightingDocument>> {

    public GetSightings() {
        this(0, 10, null);
    }

    @HandleQuery
    List<SightingDocument> getSightings() {
        return Fluxzero.search(Sighting.class)
                .lookAhead(StringUtils.trimToNull(filter), "sightingId")
                .sortByTimestamp(true)
                .skip(page * pageSize)
                .streamHits(Sighting.class, pageSize)
                .limit(pageSize)
                .map(SightingDocument::new)
                .toList();
    }
}
