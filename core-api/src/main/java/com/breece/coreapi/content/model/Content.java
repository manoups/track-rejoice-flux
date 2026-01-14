package com.breece.coreapi.content.model;

import com.breece.coreapi.proposed.sighting.model.ProposedSighting;
import com.breece.coreapi.sighting.model.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.common.search.Facet;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.modeling.Member;
import jakarta.validation.constraints.NotNull;
import lombok.With;

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
