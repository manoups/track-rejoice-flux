package com.breece.content.query.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.ExtraDetails;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.sdk.persisting.search.SearchHit;

import java.time.Duration;
import java.time.Instant;

public record ContentDocument(ContentId contentId, SightingDetails lastConfirmedSighting, ExtraDetails details, UserId ownerId, boolean online, Duration duration, Instant timestamp) {
    public ContentDocument(SearchHit<Content> searchHit) {
        this(searchHit.getValue(), searchHit.getTimestamp());
    }

    public ContentDocument(Content content, Instant timestamp) {
        this(content.contentId(), content.lastConfirmedSighting(), content.details(), content.ownerId(), content.online(), content.duration(), timestamp);
    }
}
