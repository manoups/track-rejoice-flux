package com.breece.content.command.api;

import com.breece.common.model.Content;
import com.breece.common.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import org.wildfly.common.annotation.NotNull;

public record DeleteContent(@NotNull ContentId contentId) implements ContentUpdateForOwner {

    @Apply
    Content create(Sender sender) {
        return null;
    }
}
