package com.breece.content.command.api;

import com.breece.common.model.ContentId;
import com.breece.common.model.ContentStatus;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.Stateful;
import lombok.With;

@Stateful
@Consumer(name = "content-consumer", ignoreSegment = true)
public record ContentState(@Association @EntityId ContentId contentId, @With ContentStatus status) {

    @HandleEvent
    static ContentState on(CreateContent event) {
        return new ContentState(event.contentId(), ContentStatus.OFFLINE);
    }

    @HandleEvent
    ContentState on(TakeContentOffline event) { return withStatus(ContentStatus.OFFLINE);}

    @HandleEvent
    ContentState on(PublishContent event) { return withStatus(ContentStatus.ONLINE);}
    /*Used only for the basic (publishing) service
    * Cannot buy extra service without the basic service*/
//    TODO: check how to break circular dependency
    /*@HandleEvent
    ContentState on(PlaceOrder order) {
        if (order.details().serviceIds().stream().map(Fluxzero::loadAggregate)
                .filter(Entity::isPresent)
                .map(Entity::get)
                .anyMatch(Service::basicOnline)) {
            return withStatus(ContentStatus.PENDING_PAYMENT);
        }
        return this;
    }

    @HandleEvent
    ContentState on(ValidateOrder order) {
        Order order1 = Fluxzero.loadEntity(order.getOrderId()).get();
        Content content = Fluxzero.loadAggregate(order1.contentId()).get();
        Sighting sighting = Fluxzero.sendCommandAndWait(new CreateSighting(new SightingId(), content.lastConfirmedSighting()));
        Fluxzero.sendAndForgetCommand(new ClaimSighting(content.contentId(), sighting.sightingId(), content.lastConfirmedSighting()));

        return withStatus(ContentStatus.ENABLED);
    }*/

    @HandleEvent
    ContentState on(DeleteContent order) {
        return null;
    }
}
