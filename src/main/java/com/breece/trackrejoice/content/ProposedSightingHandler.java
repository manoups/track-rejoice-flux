package com.breece.trackrejoice.content;

import com.breece.trackrejoice.content.command.AcceptProposal;
import com.breece.trackrejoice.content.command.RemoveMemberProposal;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
public class ProposedSightingHandler {
    @HandleEvent
    void on(AcceptProposal event) {
        Fluxzero.sendAndForgetCommand(new RemoveMemberProposal(event.proposedSightingId()));
    }
}
