package com.breece.content.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import org.wildfly.common.annotation.NotNull;

public record DeleteContent(@NotNull ContentId contentId) implements ContentUpdateOwner {

    @Apply
    Content create(Sender sender) {
        return null;
    }
}
