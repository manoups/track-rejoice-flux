package com.breece.content.command.api;

import com.breece.coreapi.content.model.Content;
import com.breece.coreapi.content.model.ContentId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

public record TakeContentOffline(ContentId contentId) implements ContentUpdate {
    @Apply
    Content takeOffline(Content content) {
        return content.withOnline(false);
    }
}
