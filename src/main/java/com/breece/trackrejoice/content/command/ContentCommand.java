package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.TrackSelf;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@TrackSelf
public interface ContentCommand {
    @NotNull
    ContentId contentId();

    @HandleCommand
    default Content handle(ContentCommand command) {
        return Fluxzero.loadEntity(command.contentId()).assertAndApply(command).get();
    }
}
