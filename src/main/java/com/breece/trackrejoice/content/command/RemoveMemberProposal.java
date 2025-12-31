package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.ProposedSighting;
import com.breece.trackrejoice.content.model.ProposedSightingId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record RemoveMemberProposal(@NotNull ProposedSightingId proposedSightingId) implements ProposedSightingUpdate {
    @Apply
    ProposedSighting apply(ProposedSighting proposedSighting) {
        return null;
    }
}
