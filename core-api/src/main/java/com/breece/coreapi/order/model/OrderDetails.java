package com.breece.coreapi.order.model;

import com.breece.coreapi.service.model.ServiceId;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.With;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public record OrderDetails(@NotEmpty List<ServiceId> serviceIds, @With Instant updatedAt, @NotNull @With Duration duration) {
}
