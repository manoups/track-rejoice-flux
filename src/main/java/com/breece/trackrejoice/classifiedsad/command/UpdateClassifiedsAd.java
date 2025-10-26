package com.breece.trackrejoice.classifiedsad.command;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAd;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import com.breece.trackrejoice.classifiedsad.model.ExtraDetails;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import org.wildfly.common.annotation.NotNull;

public record UpdateClassifiedsAd(@NotNull ClassifiedsAdId classifiedsAdId, @Valid @NotNull ExtraDetails details) implements ClassifiedsAdUpdate {
    @Apply
    ClassifiedsAd apply(ClassifiedsAd classifiedsAd) {
        return classifiedsAd.withDetails(details);
    }
}
