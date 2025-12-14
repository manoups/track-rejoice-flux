package com.breece.trackrejoice.content.model;

import com.breece.trackrejoice.sighting.api.model.Sighting;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Keys extends ImmobileTarget{
    @NotBlank
    String description;

    public Keys(String description, Sighting lastSeenLocation) {
        this.subtype = "keys";
        this.description = description;
        this.sighting = lastSeenLocation;
    }
}
