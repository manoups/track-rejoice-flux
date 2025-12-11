package com.breece.trackrejoice.content.query;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

public record GetClassifiedAdHistory()  implements Request<Content> {

    @HandleQuery
    Content find(ContentId id, Sender sender) {
        return Fluxzero.loadAggregate(id).get();
    }
}
