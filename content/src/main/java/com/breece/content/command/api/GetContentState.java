package com.breece.content.command.api;

import com.breece.coreapi.content.model.ContentId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;


public record GetContentState(ContentId contentId) implements Request<ContentState> {
//TODO: Add contentState query if necessary
    @HandleQuery
    ContentState getState() {
        return Fluxzero.search(ContentState.class)
                .match(contentId,"contentId")
                .fetchFirstOrNull();
    }
}
