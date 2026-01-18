package com.breece.util;

import com.breece.service.api.GetServices;
import com.breece.service.api.model.Service;
import com.breece.service.api.model.ServiceDetails;
import com.breece.service.api.model.ServiceId;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.LocalHandler;

import java.math.BigDecimal;
import java.util.List;

@LocalHandler
public class MockQueryHandler {
    @HandleQuery
    public List<Service> handle(GetServices request) {
        return List.of(Service.builder()
                .serviceId(new ServiceId("1"))
                .serviceDetails(new ServiceDetails("Basic service", "Some service description", new BigDecimal("5.00")))
                .basic(true)
                .online(true)
                .build(),
                Service.builder()
                        .serviceId(new ServiceId("2"))
                        .serviceDetails(new ServiceDetails("Basic service offline", "Some service description", new BigDecimal("5.00")))
                        .basic(true)
                        .online(false)
                        .build());
    }
}
