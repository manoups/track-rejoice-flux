package com.breece.trackrejoice.service.api.command;

import com.breece.trackrejoice.service.api.model.ServiceId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

public interface ServiceCommand {
    @NotNull
    ServiceId serviceId();

    @HandleCommand
    default void handle() {
        Fluxzero.loadAggregate(serviceId()).assertAndApply(this);
    }
}
