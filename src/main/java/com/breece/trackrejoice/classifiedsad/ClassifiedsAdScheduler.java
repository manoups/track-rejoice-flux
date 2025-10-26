package com.breece.trackrejoice.classifiedsad;

import com.breece.trackrejoice.classifiedsad.command.ClassifiedsAdUpdate;
import com.breece.trackrejoice.classifiedsad.command.TakeClassifiedsAdOffline;
import com.breece.trackrejoice.orders.api.model.Order;
import com.breece.trackrejoice.payments.api.PaymentAccepted;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
public class ClassifiedsAdScheduler {
    @HandleEvent
    void handle(PaymentAccepted event) {
        Fluxzero.loadAggregate(event.reference(), Order.class).ifPresent(orderEntity ->
        {
            Fluxzero.scheduleCommand(new TakeClassifiedsAdOffline(orderEntity.get().details().classifiedsAdId()),
                    orderEntity.get().details().duration());
            return orderEntity;
        });
    }

    @HandleEvent(allowedClasses = {CompleteClassifiedsAd.class, CancelClassifiedsAd.class})
    void handle(ClassifiedsAdUpdate event) {
        Fluxzero.cancelSchedule(new TakeClassifiedsAdOffline(event.classifiedsAdId()));
    }
}
