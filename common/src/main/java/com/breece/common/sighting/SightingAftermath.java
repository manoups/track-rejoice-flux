package com.breece.common.sighting;

import com.breece.common.sighting.model.SightingId;

public interface SightingAftermath {
    SightingId sightingId();
    boolean removeAfterMatching();
}
