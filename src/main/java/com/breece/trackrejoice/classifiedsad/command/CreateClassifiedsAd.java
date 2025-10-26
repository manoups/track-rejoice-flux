package com.breece.trackrejoice.classifiedsad.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.classifiedsad.ClassifiedAdErrors;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAd;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import com.breece.trackrejoice.classifiedsad.model.ExtraDetails;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import org.wildfly.common.annotation.NotNull;

public record CreateClassifiedsAd(@NotNull ClassifiedsAdId classifiedsAdId, @Valid @NotNull ExtraDetails details) implements ClassifiedsAdCommand {
    @AssertLegal
    void assertNew(ClassifiedsAd classifiedsAd) { throw ClassifiedAdErrors.alreadyExists; }

    @Apply
    ClassifiedsAd create(Sender sender) {
        return new ClassifiedsAd(classifiedsAdId, details, sender.userId(), false);
    }
}
