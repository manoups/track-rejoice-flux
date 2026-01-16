package com.breece.sighting.command.api;


import com.breece.common.model.ContentId;
import com.breece.common.model.ExtraDetails;
import com.breece.common.sighting.model.SightingId;

public record LinkSightingToContent(ContentId contentId, ExtraDetails details, SightingId sightingId) {
}
