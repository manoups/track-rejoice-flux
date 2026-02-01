package com.breece.proposal.api;

import com.breece.proposal.api.model.LinkedSighting;
import com.breece.proposal.api.model.LinkedSightingId;
import com.breece.proposal.api.model.LinkedSightingSeekerUpdate;
import com.breece.proposal.api.model.LinkedSightingStatus;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record RejectProposal(@NotNull LinkedSightingId linkedSightingId) implements LinkedSightingSeekerUpdate {

    @Apply
    LinkedSighting apply(LinkedSighting linkedSighting) {
        return linkedSighting.withStatus(LinkedSightingStatus.REJECTED);
    }
}