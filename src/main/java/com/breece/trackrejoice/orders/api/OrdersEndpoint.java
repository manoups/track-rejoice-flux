package com.breece.trackrejoice.orders.api;

import com.breece.trackrejoice.orders.api.model.OrderDetails;
import com.breece.trackrejoice.orders.api.model.OrderId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import org.springframework.stereotype.Component;

@Component
@Path("/orders")
public class OrdersEndpoint {

    @HandlePost
    void placeOrder(OrderDetails details) {
        Fluxzero.sendCommandAndWait(new PlaceOrder(Fluxzero.generateId(OrderId.class), details));
    }

}
