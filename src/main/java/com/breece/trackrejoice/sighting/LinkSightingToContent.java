package com.breece.trackrejoice.sighting;

import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ExtraDetails;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import jakarta.validation.constraints.NotNull;

public record LinkSightingToContent(ContentId contentId, ExtraDetails details, SightingId sightingId) {
}
