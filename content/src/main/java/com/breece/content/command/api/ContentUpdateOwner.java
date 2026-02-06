package com.breece.content.command.api;

import com.breece.content.ContentErrors;
import com.breece.content.api.model.Content;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.modeling.AssertLegal;

public interface ContentUpdateOwner extends ContentUpdate {
    @AssertLegal
    default void assertAuthorized(Content content, Sender sender) {
        if (sender.nonAuthorizedFor(content.ownerId())) {
            throw ContentErrors.unauthorized;
        }
    }
}
