package com.breece.trackrejoice.sighting.api;

import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import jakarta.validation.constraints.NotNull;

public interface SightingContentBridge {
    @NotNull
    SightingId sightingId();
    @NotNull
    ContentId contentId();
}
