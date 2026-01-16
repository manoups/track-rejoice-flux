package com.breece.content.command.api;

import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.content.ContentErrors;
import com.breece.coreapi.content.model.Content;
import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.content.model.ExtraDetails;
import com.breece.coreapi.sighting.model.SightingDetails;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record CreateContent(@NotNull ContentId contentId, @NotNull SightingDetails sightingDetails,
                            @Valid @NotNull ExtraDetails details) implements ContentCommand, com.breece.coreapi.sighting.ConfirmedSightingUpdate {
    @AssertLegal
    void assertNew(Content content) {
        throw ContentErrors.alreadyExists;
    }

    @Apply
    Content create(Sender sender) {
        return new Content(contentId, sightingDetails, List.of(), details, sender.userId(), false);
    }
}
