package com.breece.content.command.api;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
@Consumer(name = "content-scheduler")
public class ContentScheduler {
    @HandleEvent(allowedClasses = {CompleteContent.class, CancelContent.class})
    void handle(ContentUpdate event) {
        Fluxzero.cancelSchedule(new TakeContentOffline(event.contentId()));
    }

    @HandleEvent
    void handle(PublishContent event) {
        Fluxzero.scheduleCommand(new TakeContentOffline(event.contentId()), event.duration());
    }
}
