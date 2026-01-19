package com.breece.content.api.model;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Id;

public class ContentId extends Id<Content> {
    public ContentId() {
        this(Fluxzero.generateId());
    }
    public ContentId(String id) {
        super(id, "content-");
    }
}
