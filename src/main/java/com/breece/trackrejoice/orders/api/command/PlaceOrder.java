package com.breece.trackrejoice.orders.api.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAd;
import com.breece.trackrejoice.orders.api.OrderErrors;
import com.breece.trackrejoice.orders.api.model.Order;
import com.breece.trackrejoice.orders.api.model.OrderDetails;
import com.breece.trackrejoice.orders.api.model.OrderId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record PlaceOrder(OrderId orderId, @NotNull @Valid OrderDetails details) implements OrderCommand {

    @AssertLegal
    void assertNew(Order order) { throw OrderErrors.alreadyExists;}

    @AssertLegal
    void assertUnique() {
        if (0 < Fluxzero.search(Order.class).match(details().classifiedsAdId(), "details.classifiedsAdId").count()) {
            throw  OrderErrors.alreadyPlaced;
        }
    }

    @AssertLegal
    void assertExistingProduct(Sender sender) {
        if (1 != Fluxzero.search(ClassifiedsAd.class)
                .match(details().classifiedsAdId(), "classifiedsAdId")
                .match(sender.isAdmin() ? null : sender.userId(), "ownerId")
                .count()) {
            throw OrderErrors.productNotFound;
        }
    }

    @Apply
    Order apply(Sender sender) {
        return new Order(orderId, sender.userId(), details);
    }

}
