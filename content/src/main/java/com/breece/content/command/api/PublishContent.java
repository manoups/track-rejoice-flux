package com.breece.content.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.RequiresRole;
import com.breece.coreapi.authentication.Role;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;

@RequiresRole(Role.ADMIN)
public record PublishContent(@NotNull ContentId contentId, Duration duration) implements ContentUpdate {

    @Apply
    Content publish(Content content) {
        return content.withOnline(true);
    }
}
