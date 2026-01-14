package com.breece.order.api;

import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.order.model.OrderDetails;
import com.breece.coreapi.order.model.OrderId;
import com.breece.order.api.command.PlaceOrder;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import io.fluxzero.sdk.web.PathParam;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Path("/orders")
public class OrdersEndpoint {

    @HandlePost(value = {"{content-id}","{content-id}/"})
    void placeOrder(@PathParam("content-id") ContentId contentId, OrderDetails details) {
        Fluxzero.sendCommandAndWait(new PlaceOrder(Fluxzero.generateId(OrderId.class), contentId, details.withUpdatedAt(Fluxzero.currentTime()).withDuration(Duration.ofDays(90))));
    }

}
