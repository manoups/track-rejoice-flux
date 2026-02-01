package com.breece.proposal.api;

import com.breece.proposal.api.model.LinkedSightingId;
import com.breece.proposal.api.model.LinkedSightingSeekerUpdate;
import jakarta.validation.constraints.NotNull;

public record AcceptProposal(@NotNull LinkedSightingId linkedSightingId) implements LinkedSightingSeekerUpdate {
}
