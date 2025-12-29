package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.model.Content;
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
