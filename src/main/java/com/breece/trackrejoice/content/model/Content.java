package com.breece.trackrejoice.content.model;

import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.breece.trackrejoice.user.api.UserId;
import io.fluxzero.common.search.Facet;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.modeling.Member;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

@Aggregate(searchable = true)
public record Content(@EntityId ContentId contentId,
                      @NotNull
                      @With
                      SightingDetails lastConfirmedSighting,
                      @Member
                      @With
                      List<ProposedSighting> proposedSightings,
                      @With @Facet ExtraDetails details, UserId ownerId,
                      @With boolean online) {
}
