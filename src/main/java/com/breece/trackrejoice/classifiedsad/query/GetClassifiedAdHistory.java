package com.breece.trackrejoice.classifiedsad.query;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAd;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

public record GetClassifiedAdHistory()  implements Request<ClassifiedsAd> {

    @HandleQuery
    ClassifiedsAd find(ClassifiedsAdId id, Sender sender) {
        return Fluxzero.loadAggregate(id).get();
    }
}
