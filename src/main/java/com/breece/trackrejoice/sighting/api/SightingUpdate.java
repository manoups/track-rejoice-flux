package com.breece.trackrejoice.sighting.api;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.sighting.SightingErrors;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import io.fluxzero.sdk.modeling.AssertLegal;

public interface SightingUpdate extends SightingInteract {
    @AssertLegal
    default void assertAuthorized(Sighting sighting, Sender sender) {
        if (!sender.isAuthorizedFor(sighting.source())) {
            throw SightingErrors.unauthorized;
        }
    }
}
