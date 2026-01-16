package com.breece.sighting.command.api;

import com.breece.common.sighting.SightingErrors;
import com.breece.common.sighting.model.Sighting;
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
