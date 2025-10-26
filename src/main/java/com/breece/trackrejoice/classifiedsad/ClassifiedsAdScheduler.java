package com.breece.trackrejoice.classifiedsad;

import com.breece.trackrejoice.classifiedsad.command.ClassifiedsAdUpdate;
import com.breece.trackrejoice.classifiedsad.command.DeleteClassifiedsAd;
import com.breece.trackrejoice.payments.api.PaymentConfirmed;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
public class ClassifiedsAdScheduler {
    @HandleEvent
    void handle(PaymentConfirmed event) {
        Fluxzero.loadAggregate(event.classifiedsAdId()).ifPresent( ad ->
        {
            Fluxzero.scheduleCommand(new DeleteClassifiedsAd(event.classifiedsAdId()),
                    ad.get().details().getDeadline());
            return ad;
        });
    }

    @HandleEvent(allowedClasses = {CompleteClassifiedsAd.class, CancelClassifiedsAd.class})
    void handle(ClassifiedsAdUpdate event) {
        Fluxzero.cancelSchedule(new DeleteClassifiedsAd(event.classifiedsAdId()));
    }
}
