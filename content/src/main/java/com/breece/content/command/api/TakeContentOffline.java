package com.breece.content.command.api;

import com.breece.common.model.Content;
import com.breece.common.model.ContentId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

public record TakeContentOffline(ContentId contentId) implements ContentUpdateForOwner {
    @Apply
    Content takeOffline(Content content) {
        return content.withOnline(false);
    }
}
