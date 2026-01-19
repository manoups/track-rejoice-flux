package com.breece.order;

import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentState;
import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserDetails;
import com.breece.coreapi.user.api.model.UserProfile;
import com.breece.order.api.order.OrderErrors;
import com.breece.order.api.command.CreateOrder;
import com.breece.order.api.order.model.OrderDetails;
import com.breece.order.api.order.model.OrderId;
import com.breece.service.api.model.ServiceId;
import com.breece.util.MockQueryHandler;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;

class CreateOrderTest {
    TestFixture testFixture = TestFixture.create(ContentState.class, new MockQueryHandler()).givenCommands("../content/create-content.json");

    CreateOrder order1 = new CreateOrder(new OrderId("1"), new ContentId("1"), new OrderDetails(
            listOf(new ServiceId("1")), Instant.now()), "1");
    CreateOrder order2 = new CreateOrder(new OrderId("2"), new ContentId("1"), new OrderDetails(
            listOf(new ServiceId("1")), Instant.now()), "2");

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
        testFixture
                .whenCommand(new CreateOrder(new OrderId("1"), new ContentId("1"), new OrderDetails(
                        listOf(new ServiceId("2")), Instant.now()), "1"))
                .expectError(OrderErrors.serviceNotFound);
    }
}