package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.SightingErrors;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.persisting.eventsourcing.InterceptApply;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

@RequiresUser
@Data
@Accessors(fluent = true)
public class ClaimSighting implements ContentUpdate {
    @NotNull
    final ContentId contentId;
    @NotNull
    final SightingId sightingId;
    SightingDetails sightingDetails;

    @InterceptApply
    ClaimSighting interceptApply() {
        Fluxzero.loadAggregate(sightingId).ifPresent(entity -> {
            this.sightingDetails(entity.get().details());
            return entity;
        });
        return this;
    }

    @AssertLegal
    void assertSightingExists() {
        if (Fluxzero.loadAggregate(sightingId).isEmpty()) {
            throw SightingErrors.notFound;
        }
    }

    @Apply
    Content assign(Content content) {
        return content.withLastConfirmedSighting(sightingDetails);
    }
}
