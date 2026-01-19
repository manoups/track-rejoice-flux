package com.breece.content.api;

import com.breece.content.api.model.ContentId;
import com.breece.sighting.api.model.SightingId;
import jakarta.validation.constraints.NotNull;

public interface SightingContentBridge {
    @NotNull
    SightingId sightingId();
    @NotNull
    ContentId contentId();
}
