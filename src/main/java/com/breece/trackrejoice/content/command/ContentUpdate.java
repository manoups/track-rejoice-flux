package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.model.Content;
import io.fluxzero.sdk.modeling.AssertLegal;

public interface ContentUpdate extends ContentInteract {
    @AssertLegal
    default void assertAuthorized(Content content, Sender sender) {
        if (!sender.isAuthorizedFor(content.ownerId())) {
            throw ContentErrors.unauthorized;
        }
    }
}
