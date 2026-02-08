package com.breece.sighting.command.api;

import com.breece.coreapi.common.SightingDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateSightingDTO(@Valid @NotNull SightingDetails sightingDetails, boolean removeAfterMatching) {
}
