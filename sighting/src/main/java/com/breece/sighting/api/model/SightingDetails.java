package com.breece.sighting.api.model;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

//Value Object
public record SightingDetails(@NotNull BigDecimal lng, @NotNull BigDecimal lat) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SightingDetails that = (SightingDetails) o;
        return lng.compareTo(that.lng) == 0 && lat.compareTo(that.lat) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lng, lat);
    }
}
