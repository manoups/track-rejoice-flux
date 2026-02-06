package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentCommand;
import com.breece.coreapi.common.SightingDetails;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.persisting.eventsourcing.InterceptApply;
import jakarta.validation.constraints.NotNull;
import lombok.With;

public record SagaPattern (@NotNull SightingId sightingId,@NotNull ContentId contentId, @With SightingDetails details) implements ContentCommand {
    @InterceptApply
    SagaPattern interceptApply() {
        Sighting sighting = Fluxzero.loadAggregate(sightingId).get();
        return this.withDetails(sighting.details());
//        return new SagaPattern(sightingId, contentId, sighting.details());
    }

    @Apply
    Content apply(Content content) {
        return content.withLastConfirmedSighting(details);
    }


}
