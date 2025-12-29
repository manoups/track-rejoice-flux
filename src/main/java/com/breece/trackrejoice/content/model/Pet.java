package com.breece.trackrejoice.content.model;

import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import io.fluxzero.sdk.modeling.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
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

    public Pet(String name, String breed, @NonNull GenderEnum gender, SightingDetails sighting) {
        this.subtype = "pet";
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.lastConfirmedSighting = sighting;
    }
}
