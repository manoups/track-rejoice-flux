package com.breece.sighting.command.api;

import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.common.SightingEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateSightingDTO(@Valid @NotNull SightingDetails sightingDetails, boolean removeAfterMatching, @NotNull SightingEnum subtype) {
}
