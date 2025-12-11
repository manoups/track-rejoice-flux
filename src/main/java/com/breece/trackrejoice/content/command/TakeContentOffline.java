package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

public record TakeContentOffline(ContentId contentId) implements ContentUpdate {
    @Apply
    Content takeOffline(Content content) {
        return content.withOnline(false);
    }
}
