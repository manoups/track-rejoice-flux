package com.breece.trackrejoice.content.query;

import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ContentState;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;


public record GetClassifiedAdState(ContentId contentId) implements Request<ContentState> {

    @HandleQuery
    ContentState getState() {
        return Fluxzero.search(ContentState.class)
                .match(contentId,"contentId")
                .fetchFirstOrNull();
    }
}
