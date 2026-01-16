package com.breece.common.sighting;

import com.breece.common.model.ContentId;
import com.breece.common.sighting.model.SightingId;
import jakarta.validation.constraints.NotNull;

public interface SightingContentBridge {
    @NotNull
    SightingId sightingId();
    @NotNull
    ContentId contentId();
}
