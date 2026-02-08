package com.breece.content.command.api;

import com.breece.content.api.model.ExtraDetails;
import com.breece.coreapi.common.SightingDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateContentDTO(@NotNull SightingDetails sightingDetails,
                               @Valid @NotNull ExtraDetails details) {
}
