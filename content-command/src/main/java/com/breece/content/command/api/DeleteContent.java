package com.breece.content.command.api;

import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.content.model.Content;
import com.breece.coreapi.content.model.ContentId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import org.wildfly.common.annotation.NotNull;

public record DeleteContent(@NotNull ContentId contentId) implements ContentUpdate {

    @Apply
    Content create(Sender sender) {
        return null;
    }
}
