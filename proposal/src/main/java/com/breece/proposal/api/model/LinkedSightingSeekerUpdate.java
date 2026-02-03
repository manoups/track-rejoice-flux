package com.breece.proposal.api.model;

import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.api.LinkedSightingErrors;
import io.fluxzero.sdk.modeling.AssertLegal;

public interface LinkedSightingSeekerUpdate extends LinkedSightingUpdate {
    @AssertLegal
    default void assertPermitted(LinkedSighting linkedSighting, Sender sender) {
        if (sender.nonAuthorizedFor(linkedSighting.seeker())) {
            throw LinkedSightingErrors.unauthorized;
        }
    }
}
