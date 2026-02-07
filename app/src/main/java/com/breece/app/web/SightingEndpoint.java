package com.breece.app.web;

import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.command.api.CreateSightingPayload;
import com.breece.sighting.command.api.DeleteSighting;
import com.breece.sighting.query.api.GetSighting;
import com.breece.sighting.query.api.GetSightings;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/api/sighting")
public class SightingEndpoint {
    @HandlePost(value = {"", "/"})
    SightingId createSighting(CreateSightingPayload sighting) {
        var sightingId = new SightingId();
        Fluxzero.sendCommandAndWait(new CreateSighting(sightingId, sighting.sightingDetails(), sighting.removeAfterMatching()));
        return sightingId;
    }

    @HandleGet(value = {"", "/"})
    List<Sighting> getSightings(@QueryParam("page") Integer page, @QueryParam("page-size") Integer pageSize, @QueryParam("filter") String filter) {
        return Fluxzero.queryAndWait(new GetSightings(page, pageSize, filter));
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
