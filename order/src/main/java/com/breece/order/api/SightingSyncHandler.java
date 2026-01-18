package com.breece.order.api;

import com.breece.coreapi.util.GeometryUtil;
import com.breece.order.geo.model.GeoJsonPost;
import com.breece.order.geo.repositories.GeoJsonPostRepository;
import com.breece.sighting.command.api.LinkSightingToContent;
import com.breece.sighting.command.api.PostSighting;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.jooby.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Consumer(name = "sighting-sync-handler")
public class SightingSyncHandler {
    private final GeoJsonPostRepository repository;

    @HandleEvent
    void handle(PostSighting postSighting) {
        repository.save(GeoJsonPost.builder().sightingId(postSighting.sightingId().getId()).lastSeenLocation(GeometryUtil.parseLocation(postSighting.sightingDetails().lat(), postSighting.sightingDetails().lng())).build());
    }

    @HandleEvent
    @Transactional
    void handle(LinkSightingToContent linkSightingToContent) {
        repository.findByContentId(linkSightingToContent.contentId().getId()).ifPresent(repository::delete);
        GeoJsonPost geoJsonPost = repository.findBySightingId(linkSightingToContent.sightingId().getId()).orElseThrow(() -> new RuntimeException("Sighting not found"));
        geoJsonPost.setContentId(linkSightingToContent.contentId().getId());
        geoJsonPost.setDetails(linkSightingToContent.details().toString());
        repository.save(geoJsonPost);
    }
}
