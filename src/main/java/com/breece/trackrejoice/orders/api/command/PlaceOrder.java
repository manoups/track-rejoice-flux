package com.breece.trackrejoice.orders.api.command;

import com.breece.trackrejoice.orders.api.OrderErrors;
import com.breece.trackrejoice.orders.api.model.Order;
import com.breece.trackrejoice.orders.api.model.OrderDetails;
import com.breece.trackrejoice.orders.api.model.OrderId;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.NoUserRequired;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@NoUserRequired
public record PlaceOrder(OrderId orderId, @NotNull @Valid OrderDetails details) implements OrderCommand {

    @AssertLegal
    void assertNew(Order order) { throw OrderErrors.alreadyExists;}

    @Apply
    Order apply() {
        return new Order(orderId, details);
    }

}
