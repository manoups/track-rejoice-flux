package com.breece.order.api.order.model;

import com.breece.service.api.model.ServiceId;
import jakarta.validation.constraints.NotEmpty;
import lombok.With;

import java.time.Instant;
import java.util.List;

public record OrderDetails(@NotEmpty List<ServiceId> serviceIds, @With Instant updatedAt) {
}
