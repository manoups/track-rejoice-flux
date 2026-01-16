package com.breece.sighting.command.api;

import com.breece.common.sighting.SightingErrors;
import com.breece.common.sighting.model.Sighting;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.modeling.AssertLegal;

public interface SightingUpdate extends SightingInteract {
    @AssertLegal
    default void assertAuthorized(Sighting sighting, Sender sender) {
        if (!sender.isAuthorizedFor(sighting.source())) {
            throw SightingErrors.unauthorized;
        }
    }
}
