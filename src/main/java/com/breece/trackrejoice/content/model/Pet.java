package com.breece.trackrejoice.content.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.locationtech.jts.geom.Geometry;

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

    public Pet(String name, String breed, @NonNull GenderEnum gender, Geometry lastSeenLocation) {
        this.subtype = "pet";
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.lastSeenLocation = lastSeenLocation;
    }
}
