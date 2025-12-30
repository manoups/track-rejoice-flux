package com.breece.trackrejoice.sighting.api.model;

import jakarta.validation.constraints.NotNull;

//Value Object
public record SightingDetails(@NotNull Double lng, @NotNull Double lat) {
}
