package com.breece.content.command.api;

import com.breece.content.api.model.ProposedSighting;
import com.breece.content.api.model.ProposedSightingId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record RemoveMemberProposal(@NotNull ProposedSightingId proposedSightingId) implements ProposedSightingUpdate {
    @Apply
    ProposedSighting apply(ProposedSighting proposedSighting) {
        return null;
    }
}
