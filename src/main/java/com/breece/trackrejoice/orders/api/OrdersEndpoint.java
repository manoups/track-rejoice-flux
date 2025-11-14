package com.breece.trackrejoice.orders.api;

import com.breece.trackrejoice.orders.api.command.PlaceOrder;
import com.breece.trackrejoice.orders.api.model.OrderDetails;
import com.breece.trackrejoice.orders.api.model.OrderId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Path("/orders")
public class OrdersEndpoint {

    @HandlePost
    void placeOrder(OrderDetails details) {
        Fluxzero.sendCommandAndWait(new PlaceOrder(Fluxzero.generateId(OrderId.class), details.withUpdatedAt(Fluxzero.currentTime()).withDuration(Duration.ofDays(90))));
    }
//TODO: Add todo
}
