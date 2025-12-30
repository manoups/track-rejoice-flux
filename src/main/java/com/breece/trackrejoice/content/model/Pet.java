package com.breece.trackrejoice.content.model;

import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import io.fluxzero.sdk.modeling.Member;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
public class Pet extends MobileTarget {
    @NotBlank
    String name;
    @NotBlank
    String breed;
    @NonNull
    GenderEnum gender;
    @Member
    @With
    List<Sighting> proposedSightings;

    String age;
    String size;
    String color;
    String condition;
    String description;
    String location;
    String image;

    public Pet(String name, String breed, @NonNull GenderEnum gender, @Valid @NotNull SightingDetails sighting) {
        this.subtype = "pet";
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.lastConfirmedSighting = sighting;
    }

    @Override
    public ExtraDetails withLastConfirmedSighting(SightingDetails lastConfirmedSighting) {
        return new Pet(name, breed, gender, lastConfirmedSighting);
    }
}
