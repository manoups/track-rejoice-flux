package com.breece.content.command.api;

import com.breece.content.ContentErrors;
import com.breece.content.api.ConfirmedSightingUpdate;
import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.ExtraDetails;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SightingDetails;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;


public record CreateContent(@NotNull ContentId contentId, @NotNull SightingDetails sightingDetails,
                            @Valid @NotNull ExtraDetails details) implements ContentCommand, ConfirmedSightingUpdate {
    public CreateContent(CreateContentDTO payload) {
        this(new ContentId(), payload.sightingDetails(), payload.details());
    }

    @AssertLegal
    void assertNew(Content content) {
        throw ContentErrors.alreadyExists;
    }

    @Apply
    Content create(Sender sender) {
        return new Content(contentId, sightingDetails, details, sender.userId(), false, Duration.ofDays(90));
    }
}
