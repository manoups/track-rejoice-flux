package com.breece.trackrejoice.content.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Keys extends ImmobileTarget {
    @NotBlank
    String description;

    public Keys(String description) {
        this.subtype = "keys";
        this.description = description;
    }
}
