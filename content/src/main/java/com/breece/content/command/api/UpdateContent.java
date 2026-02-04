package com.breece.content.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.ExtraDetails;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import org.wildfly.common.annotation.NotNull;

public record UpdateContent(@NotNull ContentId contentId, @Valid @NotNull ExtraDetails details) implements ContentUpdateOwner {
    @Apply
    Content apply(Content content) {
        return content.withDetails(details);
    }
}
