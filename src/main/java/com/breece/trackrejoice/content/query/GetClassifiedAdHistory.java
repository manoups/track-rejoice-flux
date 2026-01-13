package com.breece.trackrejoice.content.query;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.command.ConfirmedSightingUpdate;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

import java.util.List;
import java.util.stream.Collectors;

public record GetClassifiedAdHistory(ContentId id)  implements Request<List<KeyValuePair>> {

    @HandleQuery
    List<KeyValuePair> find(Sender sender) {
        return Fluxzero.get().eventStore().getEvents(id)
                .filter(event -> ConfirmedSightingUpdate.class.isAssignableFrom(event.getPayloadClass()))
                .map(event -> new KeyValuePair(event.getTimestamp() ,((ConfirmedSightingUpdate) event.getPayload()).sightingDetails()))
                .collect(Collectors.toList());
    }
}
