package com.breece.content.api.model;

import com.breece.coreapi.common.SightingEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Keys extends ImmobileTarget {
    @NotBlank
    String description;
    @NotNull

    public Keys(@NotBlank String description, @NonNull SightingEnum subtype) {
        this.description = description;
        this.subtype = subtype;
    }
}
