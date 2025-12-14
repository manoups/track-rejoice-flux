package com.breece.trackrejoice.sighting;

import com.breece.trackrejoice.sighting.api.model.Sighting;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface SightingUpdate extends SightingCommand {
    @AssertLegal(priority = AssertLegal.HIGHEST_PRIORITY)
    default void assertExists(@Nullable Sighting sighting) {
        if (Objects.isNull(sighting)) {
            throw SightingErrors.notFound;
        }
    }
}
