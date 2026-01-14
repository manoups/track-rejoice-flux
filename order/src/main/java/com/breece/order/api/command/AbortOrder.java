package com.breece.order.api.command;

import com.breece.coreapi.authentication.RequiresRole;
import com.breece.coreapi.authentication.Role;
import com.breece.coreapi.order.OrderErrors;
import com.breece.coreapi.order.model.Order;
import com.breece.coreapi.order.model.OrderId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotBlank;

import java.time.temporal.ChronoUnit;

@RequiresRole(Role.ADMIN)
public record AbortOrder(OrderId orderId, @NotBlank String reason) implements OrderUpdate {

    @AssertLegal
    void assertEligibleForCancellation(Order order) {
        if (Fluxzero.currentTime().isAfter(order.details().updatedAt().plus(24, ChronoUnit.HOURS))) {
            throw OrderErrors.tooOld;
        }
    }

    @Apply
    Order apply(Order order) {
        return order.withAborted(true);
    }
}
