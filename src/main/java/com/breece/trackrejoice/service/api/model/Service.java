package com.breece.trackrejoice.service.api.model;

import io.fluxzero.sdk.modeling.Aggregate;
import lombok.With;

@Aggregate(searchable = true, eventSourced = false)
public record Service(ServiceId serviceId, @With ServiceDetails serviceDetails, @With boolean online) { }
