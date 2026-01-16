package com.breece.order;

import com.breece.content.command.api.ContentState;
import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.order.OrderErrors;
import com.breece.order.api.model.OrderDetails;
import com.breece.order.api.model.OrderId;
import com.breece.service.api.model.ServiceId;
import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserDetails;
import com.breece.coreapi.user.api.model.UserProfile;
import com.breece.order.api.command.PlaceOrder;
import com.breece.service.api.UpdateService;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;

class PlaceOrderTest {
    TestFixture testFixture = TestFixture.create(ContentState.class).givenCommands("service/create-service.json", "content/create-content.json");

    PlaceOrder order1 = new PlaceOrder(new OrderId("1"), new ContentId("1"), new OrderDetails(
            listOf(new ServiceId("1")), Instant.now(), Duration.ofDays(90)));
    PlaceOrder order2 = new PlaceOrder(new OrderId("2"), new ContentId("1"), new OrderDetails(
            listOf(new ServiceId("1")), Instant.now(), Duration.ofDays(90)));

    @Test
    void duplicateCreation() {
        testFixture.givenCommands(order1)
                .whenCommand(order1)
                .expectError(OrderErrors.alreadyExists);
    }

    @Test
    void multipleOrdersForSameProduct() {
        testFixture.givenCommands(order1)
                .whenCommand(order2)
                .expectError(OrderErrors.alreadyPlaced);
    }

    @Test
    void payForOtherUser() {
        testFixture.whenCommandByUser(new UserProfile(new UserId("1"), new UserDetails("name", "email"), null), order1)
                .expectError(OrderErrors.productNotFound);
    }

    @Test
    void placeOrderForOfflineService() {
        testFixture.givenCommands(new UpdateService(new ServiceId("1"), "name", "description", BigDecimal.ZERO, false))
                .whenCommand(order1)
                .expectError(OrderErrors.serviceNotFound);
    }
}