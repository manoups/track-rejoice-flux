package com.breece.trackrejoice.classifiedsad.query;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdState;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;


public record GetClassifiedAdState(ClassifiedsAdId classifiedsAdId) implements Request<ClassifiedsAdState> {

    @HandleQuery
    ClassifiedsAdState getState() {
        return Fluxzero.search(ClassifiedsAdState.class)
                .match(classifiedsAdId,"classifiedsAdId")
                .fetchFirstOrNull();
    }
}
