package com.breece.trackrejoice.classifiedsad.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Keys extends ImmobileTarget{
    @NotBlank
    String description;

    public Keys() {
        super();
        this.subtype = "keys";
    }

    public Keys(String description) {
        this();
        this.description = description;
    }
}
