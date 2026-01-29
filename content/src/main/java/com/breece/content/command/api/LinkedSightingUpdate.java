package com.breece.content.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.LinkedSighting;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface LinkedSightingUpdate extends LinkedSightingCommand {
    @AssertLegal
    default void assertExists(@Nullable LinkedSighting linkedSighting) {
        if (Objects.isNull(linkedSighting)) throw ProposedSightingErrors.notFound;
    }

    @AssertLegal
    default void assertOwner(LinkedSighting linkedSighting, Sender sender) {
        Content content = Fluxzero.<Content>loadAggregateFor(linkedSighting.linkedSightingId()).get();
        if(!sender.isAuthorizedFor(content.ownerId())) {
            throw ProposedSightingErrors.unauthorized;
        }
    }
}
