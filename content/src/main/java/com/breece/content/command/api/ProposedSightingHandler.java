package com.breece.content.command.api;

import com.breece.content.api.model.Content;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.common.Message;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Consumer(name = "proposed-sighting-handler")
public class ProposedSightingHandler {
    @HandleEvent
    void on(AcceptProposal event) {
        Fluxzero.sendAndForgetCommand(new RemoveMemberProposal(event.linkedSightingId()));
    }

    @HandleEvent
    void on(CreateProposalFacade event) {
        Fluxzero.loadAggregate(event.contentId()).mapIfPresent(Entity::get).ifPresent(content -> {
            Fluxzero.sendAndForgetCommand(Message.asMessage(
                    new CreateProposal(event.contentId(), event.sightingId(), event.linkedSightingId(), event.sightingDetails()))
                    .addUser(Sender.createSender(content.ownerId())));
        });
    }
}
