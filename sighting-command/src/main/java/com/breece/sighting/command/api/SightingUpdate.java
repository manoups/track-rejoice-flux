package com.breece.sighting.command.api;

import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.sighting.SightingErrors;
import com.breece.coreapi.sighting.model.Sighting;
import io.fluxzero.sdk.modeling.AssertLegal;

public interface SightingUpdate extends SightingInteract {
    @AssertLegal
    default void assertAuthorized(Sighting sighting, Sender sender) {
        if (!sender.isAuthorizedFor(sighting.source())) {
            throw SightingErrors.unauthorized;
        }
    }
}
