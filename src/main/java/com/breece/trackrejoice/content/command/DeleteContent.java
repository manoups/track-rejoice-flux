package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import org.wildfly.common.annotation.NotNull;

public record DeleteContent(@NotNull ContentId contentId) implements ContentUpdate {

    @Apply
    Content create(Sender sender) {
        return null;
    }
}
