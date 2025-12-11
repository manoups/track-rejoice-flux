package com.breece.trackrejoice.service.api.model;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Id;

public class ServiceId  extends Id<Service> {
    public ServiceId() { this(Fluxzero.generateId());}
    public ServiceId(String functionalId) {
        super(functionalId, "service-");
    }
}
