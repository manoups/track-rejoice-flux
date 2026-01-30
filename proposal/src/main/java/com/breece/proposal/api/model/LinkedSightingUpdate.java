package com.breece.proposal.api.model;

import com.breece.proposal.api.LinkedSightingErrors;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface LinkedSightingUpdate extends LinkedSightingCommand {
    @AssertLegal
    default void assertExists(@Nullable LinkedSighting linkedSighting) {
        if (Objects.isNull(linkedSighting)) {
            throw LinkedSightingErrors.notFound;
        }
    }
}
