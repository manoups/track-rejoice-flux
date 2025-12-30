package com.breece.trackrejoice.content.model;

import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.fluxzero.common.search.Facet;
import io.fluxzero.sdk.modeling.Member;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Pet.class, name = "Pet"),
        @JsonSubTypes.Type(value = Keys.class, name = "Keys")
})
@Getter
public abstract class ExtraDetails {
    @Facet
    String subtype;
    @NotNull
    @With
    SightingDetails lastConfirmedSighting;
    @Member
    @With
    List<Sighting> proposedSightings;
}
