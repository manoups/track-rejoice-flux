package com.breece.content.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.ExtraDetails;
import com.breece.content.api.ConfirmedSightingUpdate;
import com.breece.sighting.api.model.SightingDetails;
import com.breece.coreapi.authentication.Sender;
import com.breece.content.ContentErrors;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.util.List;


public record CreateContent(@NotNull ContentId contentId, @NotNull SightingDetails sightingDetails,
                            @Valid @NotNull ExtraDetails details) implements ContentCommand, ConfirmedSightingUpdate {
    @AssertLegal
    void assertNew(Content content) {
        throw ContentErrors.alreadyExists;
    }

    @Apply
    Content create(Sender sender) {
        return new Content(contentId, sightingDetails, List.of(), details, sender.userId(), false, Duration.ofDays(90));
    }
}
