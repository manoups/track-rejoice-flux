package com.breece.trackrejoice.sighting.api.model;

import com.breece.trackrejoice.user.api.UserId;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.persisting.search.Searchable;
import jakarta.validation.constraints.NotNull;

/*TODO: Remove Aggregate after remove GetOpenSightings*/
//@Aggregate(searchable = true)
@Searchable//(timestampPath = "details.releaseDate")
public record Sighting(@EntityId SightingId sightingId, @NotNull UserId owner, @NotNull SightingDetails details) {
}
