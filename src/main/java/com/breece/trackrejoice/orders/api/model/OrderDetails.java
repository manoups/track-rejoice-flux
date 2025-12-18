package com.breece.trackrejoice.orders.api.model;

import com.breece.trackrejoice.service.api.model.ServiceId;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.With;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public record OrderDetails(@NotEmpty List<ServiceId> serviceIds, @With Instant updatedAt, @NotNull @With Duration duration) {
}
