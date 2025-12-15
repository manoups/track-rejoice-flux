package com.breece.trackrejoice.content.model;

import com.breece.trackrejoice.content.command.CreateContent;
import com.breece.trackrejoice.content.command.DeleteContent;
import com.breece.trackrejoice.orders.api.command.PlaceOrder;
import com.breece.trackrejoice.orders.api.command.ValidateOrder;
import com.breece.trackrejoice.orders.api.model.Order;
import com.breece.trackrejoice.sighting.api.ClaimSighting;
import com.breece.trackrejoice.sighting.api.CreateSighting;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
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
        return new ContentState(event.contentId(), ContentStatus.DRAFT);
    }

    @HandleEvent
    ContentState on(PlaceOrder order) {
        return withStatus(ContentStatus.PENDING_PAYMENT);
    }

    @HandleEvent
    ContentState on(ValidateOrder order) {
        Order order1 = Fluxzero.loadEntity(order.getOrderId()).get();
        Content content = Fluxzero.loadAggregate(order1.details().contentId()).get();
        Sighting sighting = Fluxzero.sendCommandAndWait(new CreateSighting(new SightingId(), content.details().getSighting().details()));
        Fluxzero.sendAndForgetCommand(new ClaimSighting(content.contentId(), sighting.sightingId()));

        return withStatus(ContentStatus.ENABLED);
    }

    @HandleEvent
    ContentState on(DeleteContent order) {
        return null;
    }
}
