package com.breece.content.command.api;

import com.breece.common.model.ContentId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.publishing.routing.RoutingKey;
import io.fluxzero.sdk.tracking.TrackSelf;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

@TrackSelf
public interface ContentCommand {
    @RoutingKey
    @NotNull
    ContentId contentId();

    @HandleCommand
    default void handle(ContentCommand command) {
        Fluxzero.loadEntity(command.contentId()).assertAndApply(command);
    }
}
