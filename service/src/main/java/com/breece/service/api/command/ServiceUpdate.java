package com.breece.service.api.command;

import com.breece.coreapi.service.model.Service;
import com.breece.coreapi.service.ServiceErrors;
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
