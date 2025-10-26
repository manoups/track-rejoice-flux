package com.breece.trackrejoice.classifiedsad.model;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Id;

public class ClassifiedsAdId extends Id<ClassifiedsAd> {
    public ClassifiedsAdId() {
        this(Fluxzero.generateId());
    }
    public ClassifiedsAdId(String id) {
        super(id, "ad-");
    }
}
