package com.breece.trackrejoice.payments.api;

import io.fluxzero.common.MessageType;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.common.exception.TechnicalException;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleError;
import io.fluxzero.sdk.tracking.handling.Trigger;
import org.springframework.stereotype.Component;

@Consumer(name = "command-dead-lettersq-12", minIndex = 0L)
@Component
class CommandReplayHandler {

    @HandleError(allowedClasses = TechnicalException.class, disabled = true)
    void retry(@Trigger(messageType = MessageType.COMMAND) ValidatePayment failed) {
            Fluxzero.sendCommand(failed);
    }
}
