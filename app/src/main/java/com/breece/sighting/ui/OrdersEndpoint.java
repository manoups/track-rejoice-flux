package com.breece.sighting.ui;

import com.breece.common.model.ContentId;
import com.breece.order.api.order.model.OrderDetails;
import com.breece.order.api.order.model.OrderId;
import com.breece.order.api.command.CreateOrder;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import io.fluxzero.sdk.web.PathParam;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Path("/orders")
public class OrdersEndpoint {

    @HandlePost(value = {"{content-id}","{content-id}/"})
    void placeOrder(@PathParam("content-id") ContentId contentId, OrderDetails details) {
        Fluxzero.sendCommandAndWait(new CreateOrder(Fluxzero.generateId(OrderId.class), contentId, details.withUpdatedAt(Fluxzero.currentTime()), UUID.randomUUID().toString()));
    }

}
