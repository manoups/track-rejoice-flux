package com.breece.trackrejoice.content.query;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.model.Content;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record GetContents(@NotNull @PositiveOrZero Integer page,
                          @NotNull @Positive Integer pageSize) implements Request<List<Content>> {

    public GetContents() {
        this(0, 10);
    }

    @HandleQuery
    List<Content> find(Sender sender) {
        return Fluxzero.search(Content.class)
                .match(sender.isAdmin() ? null : sender.userId(), "ownerId")
                .sortBy("contentId")
                .skip(page * pageSize)
                .fetch(pageSize);
    }
}
