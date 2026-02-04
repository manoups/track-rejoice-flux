package com.breece.content.command.api;

import com.breece.content.ContentErrors;
import com.breece.content.api.model.Content;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface ContentUpdate extends ContentCommand {
    @AssertLegal
    default void assertExists(@Nullable Content content) {
        if (Objects.isNull(content)) {
            throw ContentErrors.notFound;
        }
    }
}
