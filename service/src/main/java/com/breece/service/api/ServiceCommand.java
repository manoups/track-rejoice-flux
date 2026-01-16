package com.breece.service.api;

import com.breece.service.api.model.ServiceId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.TrackSelf;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

@TrackSelf
public interface ServiceCommand {
    @NotNull
    ServiceId serviceId();

    @HandleCommand
    default void handle() {
        Fluxzero.loadAggregate(serviceId()).assertAndApply(this);
    }
}
