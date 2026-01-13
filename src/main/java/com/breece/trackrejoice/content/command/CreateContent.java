package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ExtraDetails;
import com.breece.trackrejoice.geo.GeometryUtil;
import com.breece.trackrejoice.geo.LatLng;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.With;

import java.util.List;


public record CreateContent(@NotNull ContentId contentId, @NotNull SightingDetails lastConfirmedSighting,
                            @Valid @NotNull ExtraDetails details) implements ContentCommand {
    @AssertLegal
    void assertNew(Content content) {
        throw ContentErrors.alreadyExists;
    }

    @Apply
    Content create(Sender sender) {
        return new Content(contentId, lastConfirmedSighting, List.of(), details, sender.userId(), false);
    }
}
