package com.breece.trackrejoice.classifiedsad.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAd;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import org.wildfly.common.annotation.NotNull;

public record DeleteClassifiedsAd(@NotNull ClassifiedsAdId classifiedsAdId) implements ClassifiedsAdUpdate {

    @Apply
    ClassifiedsAd create(Sender sender) {
        return null;
    }
}
