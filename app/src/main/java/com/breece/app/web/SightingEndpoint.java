package com.breece.app.web;

import com.breece.coreapi.facets.FacetPaginationRequestBody;
import com.breece.coreapi.facets.GetFacets;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.command.api.CreateSightingDTO;
import com.breece.sighting.command.api.DeleteSighting;
import com.breece.sighting.query.api.GetSighting;
import com.breece.sighting.query.api.GetSightings;
import com.breece.sighting.query.api.SightingDocument;
import io.fluxzero.common.api.search.GetFacetStatsResult;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.*;
import org.springframework.stereotype.Component;

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

    @HandlePost(value = {"list", "list/"})
    List<SightingDocument> getSightings(FacetPaginationRequestBody requestBody) {
        return Fluxzero.queryAndWait(new GetSightings(requestBody.facetFilters(), requestBody.filter(), requestBody.pagination()));
    }

    @HandlePost(value = {"list/stats", "list/stats/"})
    GetFacetStatsResult getSightingStats(FacetPaginationRequestBody requestBody) {
        return Fluxzero.queryAndWait(new GetFacets(new GetSightings(requestBody.facetFilters(), requestBody.filter(), requestBody.pagination())));
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
