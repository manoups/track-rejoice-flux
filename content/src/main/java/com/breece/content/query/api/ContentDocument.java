package com.breece.content.query.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.ExtraDetails;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import com.breece.proposal.command.api.model.LinkedSighting;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public record ContentDocument(ContentId contentId, SightingDetails lastConfirmedSighting, ExtraDetails details, UserId ownerId, boolean online, Duration duration, List<LinkedSighting> linkedSightings, Instant timestamp) {
    public ContentDocument(Content content, Instant timestamp) {
        this(content.contentId(), content.lastConfirmedSighting(), content.details(), content.ownerId(), content.online(), content.duration(), content.linkedSightings(), timestamp);
    }
}
