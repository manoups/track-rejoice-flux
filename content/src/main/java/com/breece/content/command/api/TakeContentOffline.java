package com.breece.content.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

public record TakeContentOffline(ContentId contentId) implements ContentUpdateOwner {
    @Apply
    Content takeOffline(Content content) {
        return content.withOnline(false);
    }
}
