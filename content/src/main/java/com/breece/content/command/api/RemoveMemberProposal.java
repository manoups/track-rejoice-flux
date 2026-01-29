package com.breece.content.command.api;

import com.breece.content.api.model.LinkedSighting;
import com.breece.content.api.model.LinkedSightingId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record RemoveMemberProposal(@NotNull LinkedSightingId linkedSightingId) implements LinkedSightingUpdate {
    @Apply
    LinkedSighting apply(LinkedSighting linkedSighting) {
        return null;
    }
}
