package com.breece.trackrejoice.content.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Keys extends ImmobileTarget{
    @NotBlank
    String description;

    public Keys(String description, Geometry lastSeenLocation) {
        this.subtype = "keys";
        this.description = description;
        this.lastSeenLocation = lastSeenLocation;
    }
}
