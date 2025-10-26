package com.breece.trackrejoice.classifiedsad.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.classifiedsad.ClassifiedAdErrors;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAd;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface ClassifiedsAdUpdate extends ClassifiedsAdCommand {
    @AssertLegal
    default void assertExists(@Nullable ClassifiedsAd classifiedsAd) {
        if (Objects.isNull(classifiedsAd)) {
            throw ClassifiedAdErrors.notFound;
        }
    }

    @AssertLegal
    default void assertAuthorized(ClassifiedsAd classifiedsAd, Sender sender) {
        if (!sender.isAuthorizedFor(classifiedsAd.ownerId())) {
            throw ClassifiedAdErrors.unauthorized;
        }
    }
}
