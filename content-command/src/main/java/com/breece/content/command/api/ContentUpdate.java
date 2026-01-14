package com.breece.content.command.api;

import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.content.ContentErrors;
import com.breece.coreapi.content.model.Content;
import io.fluxzero.sdk.modeling.AssertLegal;

public interface ContentUpdate extends ContentInteract {
    @AssertLegal
    default void assertAuthorized(Content content, Sender sender) {
        if (!sender.isAuthorizedFor(content.ownerId())) {
            throw ContentErrors.unauthorized;
        }
    }
}
