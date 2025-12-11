package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.ContentAdErrors;
import com.breece.trackrejoice.content.model.Content;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface ContentUpdate extends ContentCommand {
    @AssertLegal
    default void assertExists(@Nullable Content content) {
        if (Objects.isNull(content)) {
            throw ContentAdErrors.notFound;
        }
    }

    @AssertLegal
    default void assertAuthorized(Content content, Sender sender) {
        if (!sender.isAuthorizedFor(content.ownerId())) {
            throw ContentAdErrors.unauthorized;
        }
    }
}
