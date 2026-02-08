package com.breece.content.api.model;

import com.breece.coreapi.common.SightingEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Pet extends MobileTarget {
    @NotBlank
    String name;
    @NonNull
    SightingEnum subtype;
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

    public Pet(String name, String breed, @NonNull GenderEnum gender, @NonNull SightingEnum subtype) {
        this.type = "pet";
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.subtype = subtype;
    }
}
