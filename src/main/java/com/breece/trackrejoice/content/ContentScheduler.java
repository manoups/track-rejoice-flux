package com.breece.trackrejoice.content;

import com.breece.trackrejoice.content.command.ContentUpdate;
import com.breece.trackrejoice.content.command.TakeContentOffline;
import com.breece.trackrejoice.orders.api.model.Order;
import com.breece.trackrejoice.payments.api.PaymentAccepted;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
public class ContentScheduler {
    @HandleEvent
    void handle(PaymentAccepted event) {
        Fluxzero.loadAggregate(event.reference(), Order.class).ifPresent(orderEntity ->
        {
            Fluxzero.scheduleCommand(new TakeContentOffline(orderEntity.get().contentId()),
                    orderEntity.get().details().duration());
            return orderEntity;
        });
    }

    @HandleEvent(allowedClasses = {CompleteContent.class, CancelContent.class})
    void handle(ContentUpdate event) {
        Fluxzero.cancelSchedule(new TakeContentOffline(event.contentId()));
    }
}
