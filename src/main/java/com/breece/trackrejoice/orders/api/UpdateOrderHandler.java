package com.breece.trackrejoice.orders.api;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.geo.model.GeoJsonPost;
import com.breece.trackrejoice.geo.repositories.GeoJsonPostRepository;
import com.breece.trackrejoice.orders.api.command.PlaceOrder;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateOrderHandler {
    private final GeoJsonPostRepository repository;

    @HandleEvent
    void handle(PlaceOrder placeOrder) {
        Content content = Fluxzero.loadAggregate(placeOrder.details().contentId()).get();
        repository.save(GeoJsonPost.builder().orderId(content.contentId().toString()).lastSeenLocation(content.details().getSighting().details().spottedLocation()).build());
    }
}
