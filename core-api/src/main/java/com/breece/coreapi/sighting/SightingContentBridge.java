package com.breece.coreapi.sighting;

import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.sighting.model.SightingId;
import jakarta.validation.constraints.NotNull;

public interface SightingContentBridge {
    @NotNull
    SightingId sightingId();
    @NotNull
    ContentId contentId();
}
