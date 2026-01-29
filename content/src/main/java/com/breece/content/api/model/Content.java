package com.breece.content.api.model;

import com.breece.coreapi.user.WithOwner;
import com.breece.sighting.api.model.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.common.search.Facet;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.modeling.Member;
import jakarta.validation.constraints.NotNull;
import lombok.With;

import java.time.Duration;
import java.util.List;

@Aggregate(searchable = true)
public record Content(@EntityId ContentId contentId,
                      @NotNull
                      @With
                      SightingDetails lastConfirmedSighting,
                      @Member
                      @With
                      List<LinkedSighting> linkedSightings,
                      @With @Facet ExtraDetails details, UserId ownerId,
                      @With boolean online, @NotNull @With Duration duration) implements WithOwner {
}
