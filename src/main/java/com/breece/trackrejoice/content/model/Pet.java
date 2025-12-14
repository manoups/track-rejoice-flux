package com.breece.trackrejoice.content.model;

import com.breece.trackrejoice.sighting.api.model.ClaimedSighting;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    String age;
    String size;
    String color;
    String condition;
    String description;
    String location;
    String image;

    public Pet(String name, String breed, @NonNull GenderEnum gender, ClaimedSighting sighting) {
        this.subtype = "pet";
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.sighting = sighting;
    }
}
