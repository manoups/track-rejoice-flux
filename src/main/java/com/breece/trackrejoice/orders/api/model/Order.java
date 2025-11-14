package com.breece.trackrejoice.orders.api.model;

import com.breece.trackrejoice.user.api.UserId;
import io.fluxzero.sdk.modeling.Aggregate;
import lombok.With;

import static io.fluxzero.sdk.modeling.EventPublication.IF_MODIFIED;

@Aggregate(searchable = true, eventPublication = IF_MODIFIED)
@With
public record Order(OrderId orderId, UserId userId, @With String pgpId, @With String status, OrderDetails details, boolean paid, boolean aborted) {

    public Order(OrderId orderId, UserId userId, OrderDetails details) {
        this(orderId, userId,null, null, details, false, false);
    }
}
