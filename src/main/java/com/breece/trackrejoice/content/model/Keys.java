package com.breece.trackrejoice.content.model;

import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import io.fluxzero.sdk.modeling.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Keys extends ImmobileTarget {
    @NotBlank
    String description;
    @Member
    @With
    List<Sighting> proposedSightings;

    public Keys(String description, SightingDetails lastSeenLocation) {
        this.subtype = "keys";
        this.description = description;
        this.lastConfirmedSighting = lastSeenLocation;
    }

    @Override
    public ExtraDetails withLastConfirmedSighting(SightingDetails lastConfirmedSighting) {
        return new Keys(description, lastConfirmedSighting);
    }
}
