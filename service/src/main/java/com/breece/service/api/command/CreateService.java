package com.breece.service.api.command;

import com.breece.coreapi.authentication.RequiresRole;
import com.breece.coreapi.authentication.Role;
import com.breece.coreapi.service.model.Service;
import com.breece.coreapi.service.model.ServiceDetails;
import com.breece.coreapi.service.model.ServiceId;
import com.breece.coreapi.service.ServiceErrors;
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
