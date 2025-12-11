package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.ContentAdErrors;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ExtraDetails;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.Valid;
import org.wildfly.common.annotation.NotNull;

import java.util.ArrayList;

@RequiresUser
public record CreateContent(@NotNull ContentId contentId, @Valid @NotNull ExtraDetails details) implements ContentCommand {
    @AssertLegal
    void assertNew(Content content) { throw ContentAdErrors.alreadyExists; }

    @Apply
    Content create(Sender sender) {
        return new Content(contentId, details, sender.userId(), false);
    }
}
