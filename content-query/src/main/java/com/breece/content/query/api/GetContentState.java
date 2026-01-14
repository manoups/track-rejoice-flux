package com.breece.content.query.api;

import com.breece.coreapi.content.model.ContentId;


public record GetContentState(ContentId contentId) /*implements Request<ContentState>*/ {
//TODO: Add contentState query if necessary
    /*@HandleQuery
    ContentState getState() {
        return Fluxzero.search(ContentState.class)
                .match(contentId,"contentId")
                .fetchFirstOrNull();
    }*/
}
