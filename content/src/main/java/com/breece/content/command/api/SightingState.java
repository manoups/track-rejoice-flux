package com.breece.content.command.api;

import com.breece.coreapi.score.association.CreateWeightedAssociation;
import com.breece.coreapi.score.association.DeleteWeightedAssociation;
import com.breece.coreapi.score.association.WeightedAssociationId;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.command.api.DeleteSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.Stateful;

@Stateful
@Consumer(name = "content-consumer", ignoreSegment = true)
public record SightingState(@EntityId SightingId sightingId) {
    @Association("sightingId")
    @HandleEvent
    static SightingState on(CreateSighting event) {
        return new SightingState(event.sightingId());
    }

    @Association("sightingId")
    @HandleEvent
    SightingState on(DeleteSighting event) {
        return null;
    }

    @Association("contentId")
    @HandleEvent
    void on(CreateContent event) {
        Fluxzero.sendAndForgetCommand(new CreateWeightedAssociation(new WeightedAssociationId(event.contentId().getId(), sightingId.getId()), event.contentId().getId(), sightingId.getId()));
    }

    @Association("contentId")
    @HandleEvent
    void on(DeleteContent event) {
        Fluxzero.sendAndForgetCommand(new DeleteWeightedAssociation(new WeightedAssociationId(event.contentId().getId(), sightingId.getId())));
    }
}
