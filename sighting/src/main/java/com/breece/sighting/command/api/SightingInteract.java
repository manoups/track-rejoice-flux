package com.breece.sighting.command.api;

import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.Sighting;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface SightingInteract extends SightingCommand {
    @AssertLegal
    default void assertExists(@Nullable Sighting sighting) {
        if (Objects.isNull(sighting)) {
            throw SightingErrors.notFound;
        }
    }
}
