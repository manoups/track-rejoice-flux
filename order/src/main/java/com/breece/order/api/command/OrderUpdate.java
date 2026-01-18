package com.breece.order.api.command;

import com.breece.order.api.order.model.Order;
import com.breece.order.api.order.OrderErrors;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.tracking.TrackSelf;
import jakarta.annotation.Nullable;

import java.util.Objects;

@TrackSelf
public interface OrderUpdate extends OrderCommand{
    @AssertLegal(priority = AssertLegal.HIGHEST_PRIORITY)
    default void assertExists(@Nullable Order classifiedsAd) {
        if (Objects.isNull(classifiedsAd)) {
            throw OrderErrors.notFound;
        }
    }
}
