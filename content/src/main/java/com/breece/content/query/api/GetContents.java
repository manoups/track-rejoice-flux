package com.breece.content.query.api;

import com.breece.content.api.model.Content;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record GetContents(@NotNull @PositiveOrZero Integer page,
                          @NotNull @Positive Integer pageSize) implements Request<List<ContentDocument>> {

    public GetContents() {
        this(0, 10);
    }

    @HandleQuery
    List<ContentDocument> find(Sender sender) {
        return Fluxzero.search(Content.class)
                .match(sender.isAdmin() ? null : sender.userId(), "ownerId")
                .sortByTimestamp(true)
                .skip(page * pageSize)
                .streamHits(Content.class, pageSize)
                .map(ContentDocument::new)
                .limit(pageSize)
                .toList();
    }
}
