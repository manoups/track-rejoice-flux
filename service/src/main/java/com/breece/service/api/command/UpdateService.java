package com.breece.service.api.command;


import com.breece.coreapi.authentication.RequiresRole;
import com.breece.coreapi.authentication.Role;
import com.breece.coreapi.service.model.Service;
import com.breece.coreapi.service.model.ServiceDetails;
import com.breece.coreapi.service.model.ServiceId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@RequiresRole(Role.ADMIN)
public record UpdateService(@NotNull ServiceId serviceId, @NotBlank String name, @NotBlank String description, @PositiveOrZero BigDecimal price, boolean online) implements ServiceUpdate{
    @Apply
    public Service apply(Service service) {
        return service.withServiceDetails(new ServiceDetails(name, description, price)).withOnline(online);
    }
}
