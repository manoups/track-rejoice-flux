package com.breece.service.api;

import com.breece.service.api.model.Service;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

public record GetBasicService() implements Request<Service> {
    @HandleQuery
    public Service handle(GetBasicService request) {
        return Fluxzero.queryAndWait(new GetServices())
                .stream()
                .filter(Service::basicOnline)
                .findFirst()
                .orElse(null);
    }
}
