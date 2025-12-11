package com.breece.trackrejoice.service.api.command;

import com.breece.trackrejoice.service.api.ServiceErrors;
import com.breece.trackrejoice.service.api.model.Service;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface ServiceUpdate extends ServiceCommand{
    @AssertLegal(priority = AssertLegal.HIGHEST_PRIORITY)
    default void assertExists(@Nullable Service service) {
        if (Objects.isNull(service)) {
            throw ServiceErrors.notFound;
        }
    }
}
