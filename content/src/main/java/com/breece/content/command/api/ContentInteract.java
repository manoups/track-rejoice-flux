package com.breece.content.command.api;

import com.breece.content.ContentErrors;
import com.breece.coreapi.content.model.Content;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface ContentInteract extends ContentCommand {
    @AssertLegal
    default void assertExists(@Nullable Content content) {
        if (Objects.isNull(content)) {
            throw ContentErrors.notFound;
        }
    }
}
