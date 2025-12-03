package com.breece.trackrejoice.classifiedsad.query;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAd;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record GetClassifiedAds(@NotNull @PositiveOrZero Integer page,
                               @NotNull @Positive Integer pageSize) implements Request<List<ClassifiedsAd>> {

    public GetClassifiedAds() {
        this(0, 10);
    }

    @HandleQuery
    List<ClassifiedsAd> find(Sender sender) {
        return Fluxzero.search(ClassifiedsAd.class)
                .match(sender.isAdmin() ? null : sender.userId(), "ownerId")
                .sortBy("id")
                .skip(page * pageSize)
                .fetch(pageSize);
    }
}
