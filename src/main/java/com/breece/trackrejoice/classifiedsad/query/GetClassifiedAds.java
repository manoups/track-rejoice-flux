package com.breece.trackrejoice.classifiedsad.query;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAd;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

import java.util.List;

public record GetClassifiedAds() implements Request<List<ClassifiedsAd>> {

    @HandleQuery
    List<ClassifiedsAd> find(Sender sender) {
        return Fluxzero.search(ClassifiedsAd.class)
                .match(sender.isAdmin() ? null : sender.userId(), "ownerId")
                .fetchAll();
    }
}
