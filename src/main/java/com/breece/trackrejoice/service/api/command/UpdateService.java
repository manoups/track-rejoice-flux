package com.breece.trackrejoice.service.api.command;

import com.breece.trackrejoice.authentication.RequiresRole;
import com.breece.trackrejoice.authentication.Role;
import com.breece.trackrejoice.service.api.model.Service;
import com.breece.trackrejoice.service.api.model.ServiceDetails;
import com.breece.trackrejoice.service.api.model.ServiceId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@RequiresRole(Role.ADMIN)

public record UpdateService(@NotNull ServiceId serviceId, String name, String description, BigDecimal price, boolean online) implements ServiceUpdate{
    @Apply
    public Service apply(Service service) {
        return service.withServiceDetails(new ServiceDetails(name, description, price)).withOnline(online);
    }
}
