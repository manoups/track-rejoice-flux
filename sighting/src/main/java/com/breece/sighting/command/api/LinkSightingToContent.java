package com.breece.sighting.command.api;


import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.content.model.ExtraDetails;
import com.breece.coreapi.sighting.model.SightingId;

public record LinkSightingToContent(ContentId contentId, ExtraDetails details, SightingId sightingId) {
}
