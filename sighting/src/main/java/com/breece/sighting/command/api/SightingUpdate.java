package com.breece.sighting.command.api;

import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.Sighting;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.modeling.AssertLegal;

public interface SightingUpdate extends SightingInteract {
    @AssertLegal
    default void assertAuthorized(Sighting sighting, Sender sender) {
        if (sender.nonAuthorizedFor(sighting.source())) {
            throw SightingErrors.unauthorized;
        }
    }
}
