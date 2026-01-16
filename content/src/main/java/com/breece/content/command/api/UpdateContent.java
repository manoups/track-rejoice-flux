package com.breece.content.command.api;

import com.breece.coreapi.content.model.Content;
import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.content.model.ExtraDetails;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import org.wildfly.common.annotation.NotNull;

public record UpdateContent(@NotNull ContentId contentId, @Valid @NotNull ExtraDetails details) implements ContentUpdate {
    @Apply
    Content apply(Content content) {
        return content.withDetails(details);
    }
}
