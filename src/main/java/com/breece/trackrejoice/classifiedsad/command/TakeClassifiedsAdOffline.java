package com.breece.trackrejoice.classifiedsad.command;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAd;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

public record TakeClassifiedsAdOffline(ClassifiedsAdId classifiedsAdId) implements ClassifiedsAdUpdate {
    @Apply
    ClassifiedsAd takeOffline(ClassifiedsAd classifiedsAd) {
        return classifiedsAd.withOnline(false);
    }
}
