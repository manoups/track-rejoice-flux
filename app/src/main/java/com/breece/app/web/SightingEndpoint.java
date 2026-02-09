package com.breece.app.web;

import com.breece.coreapi.facets.GetFacets;
import com.breece.coreapi.facets.Pagination;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.command.api.CreateSightingDTO;
import com.breece.sighting.command.api.DeleteSighting;
import com.breece.sighting.query.api.*;
import io.fluxzero.common.api.search.FacetStats;
import io.fluxzero.common.api.search.GetFacetStatsResult;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Path("/api/sighting")
public class SightingEndpoint {
    @HandlePost(value = {"", "/"})
    SightingId createSighting(CreateSightingDTO sighting) {
        CreateSighting command = new CreateSighting(sighting);
        Fluxzero.sendCommandAndWait(command);
        return command.sightingId();
    }

    @HandleGet(value = {"", "/"})
    List<SightingDocument> getSightings(@QueryParam("page") Integer page, @QueryParam("page-size") Integer pageSize, @QueryParam("filter") String filter, @QueryParam("subtype") String subtype) {
        return Fluxzero.queryAndWait(new GetSightingsWithStats(Collections.emptyList(), null, new Pagination(0, 10)));
    }

    @HandleGet(value = {"stats", "stats/"})
    GetFacetStatsResult getSightingStats() {
        return Fluxzero.queryAndWait(new GetFacets(new GetSightingsWithStats(Collections.emptyList(), null, new Pagination(0, 10))));
    }

    @HandleGet(value = {"{id}", "{id}/"})
    Sighting getSighting(@PathParam SightingId id) {
        return Fluxzero.queryAndWait(new GetSighting(id));
    }

    @HandleDelete(value = {"{id}", "{id}/"})
    SightingId deleteSighting(@PathParam SightingId id) {
        Fluxzero.sendCommandAndWait(new DeleteSighting(id));
        return id;
    }
}
