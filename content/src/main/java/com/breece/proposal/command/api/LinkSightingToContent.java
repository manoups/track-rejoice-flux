package com.breece.proposal.command.api;


import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.ExtraDetails;
import com.breece.sighting.api.model.SightingId;

public record LinkSightingToContent(ContentId contentId, ExtraDetails details, SightingId sightingId) {
}
