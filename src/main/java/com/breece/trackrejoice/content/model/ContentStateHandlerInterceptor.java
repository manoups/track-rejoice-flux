package com.breece.trackrejoice.content.model;

import com.breece.trackrejoice.orders.api.command.PlaceOrder;
import io.fluxzero.common.handling.HandlerInvoker;
import io.fluxzero.sdk.common.serialization.DeserializingMessage;
import io.fluxzero.sdk.tracking.handling.HandlerInterceptor;

import java.util.function.Function;

public class ContentStateHandlerInterceptor implements HandlerInterceptor {
    @Override
    public Function<DeserializingMessage, Object> interceptHandling(Function<DeserializingMessage, Object> function, HandlerInvoker invoker) {
        return message -> {
            if (message.getPayload() instanceof PlaceOrder) {}
            return function.apply(message);
        };
    }
}
