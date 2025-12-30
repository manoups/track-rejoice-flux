package com.breece.trackrejoice.content.query;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotNull;

public record GetContent(@NotNull ContentId contentId) implements Request<Content> {
    @HandleQuery
    Content find(Sender sender) {
        return Fluxzero.search(Content.class)
                .match(sender.isAdmin() ? null : sender.userId(), "ownerId")
                .match(contentId, "contentId")
                .<Content>fetchFirst().orElseThrow(() -> ContentErrors.notFound);
    }
}
