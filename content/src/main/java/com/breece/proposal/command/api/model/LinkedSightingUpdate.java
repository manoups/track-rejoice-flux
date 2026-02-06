package com.breece.proposal.command.api.model;

import com.breece.content.command.api.ContentInteract;
import com.breece.proposal.command.api.LinkedSightingErrors;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface LinkedSightingUpdate extends LinkedSightingCommand, ContentInteract {
    @AssertLegal
    default void assertExists(@Nullable LinkedSighting linkedSighting) {
        if (Objects.isNull(linkedSighting)) {
            throw LinkedSightingErrors.notFound;
        }
    }
}
