package com.breece.trackrejoice.classifiedsad.model;

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
}
