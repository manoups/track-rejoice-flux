package com.breece.trackrejoice.orders.api.model;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Id;

public class OrderId  extends Id<Order> {
    public OrderId() { this(Fluxzero.generateId());}
    public OrderId(String functionalId) {
        super(functionalId, "order-");
    }
}