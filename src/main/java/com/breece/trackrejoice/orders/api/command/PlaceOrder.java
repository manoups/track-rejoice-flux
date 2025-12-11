package com.breece.trackrejoice.orders.api.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.orders.api.OrderErrors;
import com.breece.trackrejoice.orders.api.model.Order;
import com.breece.trackrejoice.orders.api.model.OrderDetails;
import com.breece.trackrejoice.orders.api.model.OrderId;
import com.breece.trackrejoice.service.api.model.Service;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record PlaceOrder(@NotNull OrderId orderId, @NotNull @Valid OrderDetails details) implements OrderCommand {

    @AssertLegal
    void assertNew(Order order) { throw OrderErrors.alreadyExists;}

    @AssertLegal
    void assertUnique() {
//        if (Fluxzero.loadAggregate(contentId).get().orders().stream().anyMatch(it -> !it.paid))
        if (0 < Fluxzero.search(Order.class).match(details.contentId(), "details.contentId").count()) {
            throw  OrderErrors.alreadyPlaced;
        }
    }

    @AssertLegal
    void assertExistingProduct(Sender sender) {
        if (1 != Fluxzero.search(Content.class)
                .match(details.contentId(), "contentId")
                .match(sender.isAdmin() ? null : sender.userId(), "ownerId")
                .count()) {
            throw OrderErrors.productNotFound;
        }
    }

    @AssertLegal
    void assertExistingService() {
        details.serviceIds().forEach(serviceId -> {
            if (1 != Fluxzero.search(Service.class)
                    .match(serviceId, "serviceId")
                    .count()) {
                throw OrderErrors.serviceNotFound;
            }
        });
    }

    @Apply
    Order apply(Sender sender) {
        return new Order(orderId, sender.userId(), details, null, false);
    }

}
