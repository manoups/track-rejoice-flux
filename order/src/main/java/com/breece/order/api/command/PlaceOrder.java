package com.breece.order.api.command;

import com.breece.common.model.Content;
import com.breece.common.model.ContentId;
import com.breece.common.model.ContentStatus;
import com.breece.content.command.api.ContentState;
import com.breece.coreapi.authentication.Sender;
import com.breece.content.ContentErrors;
import com.breece.order.api.OrderErrors;
import com.breece.order.api.model.Order;
import com.breece.order.api.model.OrderDetails;
import com.breece.order.api.model.OrderId;
import com.breece.service.api.ServiceErrors;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

@RequiresUser
public record PlaceOrder(@NotNull OrderId orderId, @NotNull ContentId contentId, @NotNull @Valid OrderDetails details) implements OrderCommand {

    @AssertLegal
    void assertNew(Order order) {
        throw OrderErrors.alreadyExists;
    }

    @AssertLegal
    void assertUnique() {
        if (0 < Fluxzero.search(Order.class).match(contentId, "contentId").count()) {
            throw OrderErrors.alreadyPlaced;
        }
    }

    @AssertLegal
    void assertExistingProduct(Sender sender) {
        Entity<Content> contentEntity = Fluxzero.loadAggregate(contentId);
        if (!contentEntity.isPresent() || (!sender.isAdmin() && !contentEntity.get().ownerId().equals(sender.userId()))) {
            throw OrderErrors.productNotFound;
        }
    }

    @AssertLegal
    void assertExistingService() {
        if (details.serviceIds().stream().map(Fluxzero::loadAggregate).anyMatch(it -> !it.isPresent() || !it.get().online())) {
            throw OrderErrors.serviceNotFound;
        }
    }

    @AssertLegal
    void assertBasicServiceIsPurchased() {
        Optional<ContentState> stateOptional = Fluxzero.search(ContentState.class).match(contentId, "contentId").fetchFirst();
        if (stateOptional.isEmpty()) {
            throw ContentErrors.notFound;
        }
        if (stateOptional.get().status() == ContentStatus.OFFLINE) {
            if (details.serviceIds().stream().map(Fluxzero::loadAggregate).noneMatch(it -> it.isPresent() && it.get().basic())) {
                throw ServiceErrors.basicServiceRequired;
            }
        }
    }

    @Apply
    Order apply(Sender sender) {
        return new Order(orderId, sender.userId(), contentId, details, null, false);
    }

}
