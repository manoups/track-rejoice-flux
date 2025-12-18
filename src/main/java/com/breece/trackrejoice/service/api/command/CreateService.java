package com.breece.trackrejoice.service.api.command;

import com.breece.trackrejoice.authentication.RequiresRole;
import com.breece.trackrejoice.authentication.Role;
import com.breece.trackrejoice.service.api.ServiceErrors;
import com.breece.trackrejoice.service.api.model.Service;
import com.breece.trackrejoice.service.api.model.ServiceDetails;
import com.breece.trackrejoice.service.api.model.ServiceId;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@RequiresRole(Role.ADMIN)
public record CreateService (@NotNull
        ServiceId serviceId, @NotBlank String name, @NotBlank String description, @PositiveOrZero BigDecimal price, boolean basic) implements ServiceCommand {

    @AssertLegal
    void assertNew(Service service) { throw ServiceErrors.alreadyExists; }

    @Apply
    Service apply() { return new Service(serviceId, new ServiceDetails(name, description, price), basic, true); }
}
