package com.breece.content.command.api;

import com.breece.common.model.Content;
import com.breece.content.ContentErrors;
import io.fluxzero.sdk.modeling.AssertLegal;

public interface ContentInteract extends ContentUpdate {

    @AssertLegal
    default void assertOnline(Content content) {
        if (!content.online()) {
            throw ContentErrors.offlineContent;
        }
    }
}
