package com.breece.content.query.api;

import com.breece.common.model.ContentId;
import com.breece.common.sighting.ConfirmedSightingUpdate;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import io.fluxzero.sdk.tracking.handling.authentication.NoUserRequired;

import java.util.List;
import java.util.stream.Collectors;

@NoUserRequired
public record GetSightingHistoryForContent(ContentId id)  implements Request<List<KeyValuePair>> {

    @HandleQuery
    List<KeyValuePair> find() {
//        TODO: Do we need to keep history private?
        /*Entity<Content> contentEntity = Fluxzero.loadAggregate(id);
        if(contentEntity.isEmpty() || !sender.isAuthorizedFor(contentEntity.get().ownerId())) {
            return List.of();
        }*/
        return Fluxzero.get().eventStore().getEvents(id)
                .filter(event -> ConfirmedSightingUpdate.class.isAssignableFrom(event.getPayloadClass()))
                .map(event -> new KeyValuePair(event.getTimestamp() ,((ConfirmedSightingUpdate) event.getPayload()).sightingDetails()))
                .collect(Collectors.toList());
    }
}
