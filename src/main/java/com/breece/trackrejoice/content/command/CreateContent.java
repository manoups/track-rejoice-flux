package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ExtraDetails;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.Valid;
import org.wildfly.common.annotation.NotNull;

@RequiresUser
public record CreateContent(@NotNull ContentId contentId, @Valid @NotNull ExtraDetails details) implements ContentCommand {
    @AssertLegal
    void assertNew(Content content) { throw ContentErrors.alreadyExists; }

    @Apply
    Content create(Sender sender) {
        return new Content(contentId, details, sender.userId(), false);
    }
}
