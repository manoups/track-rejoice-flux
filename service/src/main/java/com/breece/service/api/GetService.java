package com.breece.service.api;

import com.breece.service.api.model.Service;
import com.breece.service.api.model.ServiceId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

public record GetService(ServiceId serviceId) implements Request<Service> {
    @HandleQuery
    public Service handle(GetService request) {
        return Fluxzero.queryAndWait(new GetServices())
                .stream().filter(it -> it.serviceId().equals(request.serviceId()))
                .findFirst()
                .orElse(null);
    }
}
