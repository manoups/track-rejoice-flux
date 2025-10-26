package com.breece.trackrejoice.orders.api.command;

import com.breece.trackrejoice.orders.api.OrderErrors;
import com.breece.trackrejoice.orders.api.model.Order;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.tracking.TrackSelf;
import jakarta.annotation.Nullable;

import java.util.Objects;

@TrackSelf
public interface OrderUpdate extends OrderCommand{
    @AssertLegal(priority = Integer.MAX_VALUE)
    default void assertExists(@Nullable Order classifiedsAd) {
        if (Objects.isNull(classifiedsAd)) {
            throw OrderErrors.notFound;
        }
    }
}
