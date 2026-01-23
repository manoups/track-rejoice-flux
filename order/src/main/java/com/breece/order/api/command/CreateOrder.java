package com.breece.order.api.command;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.ContentStatus;
import com.breece.content.command.api.ContentState;
import com.breece.coreapi.authentication.Sender;
import com.breece.content.ContentErrors;
import com.breece.order.api.order.OrderErrors;
import com.breece.order.api.order.model.Order;
import com.breece.order.api.order.model.OrderDetails;
import com.breece.order.api.order.model.OrderId;
import com.breece.service.api.GetService;
import com.breece.service.api.ServiceErrors;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.Optional;

@RequiresUser
public record CreateOrder(@NotNull OrderId orderId, @NotNull ContentId contentId, @NotNull @Valid OrderDetails details, String pspReference) implements OrderCommand {

    @AssertLegal
    void assertNew(Order order) {
        throw OrderErrors.alreadyExists;
    }

    @AssertLegal
    void assertExistingProduct(Sender sender) {
        Entity<Content> contentEntity = Fluxzero.loadAggregate(contentId);
        if (!contentEntity.isPresent() || !sender.isAuthorizedFor(contentEntity.get().ownerId())) {
            throw OrderErrors.productNotFound;
        }
    }

    @AssertLegal
    void assertExistingService() {
        if (details.serviceIds().stream().map(serviceId -> Fluxzero.queryAndWait(new GetService(serviceId))).anyMatch(it -> Objects.isNull(it) || !it.online())) {
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
            if (details.serviceIds().stream().map(it -> Fluxzero.queryAndWait(new GetService(it))).noneMatch(it -> Objects.nonNull(it) && it.basic())) {
                throw ServiceErrors.basicServiceRequired;
            }
        }
    }

    @Apply
    Order apply(Sender sender) {
        return new Order(orderId, sender.userId(), contentId, details, pspReference);
    }

}
