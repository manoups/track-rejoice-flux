package com.breece.trackrejoice.orders.api.command;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import com.breece.trackrejoice.orders.api.OrderErrors;
import com.breece.trackrejoice.orders.api.model.OrderDetails;
import com.breece.trackrejoice.orders.api.model.OrderId;
import com.breece.trackrejoice.payments.api.model.PaymentId;
import com.breece.trackrejoice.user.api.UserId;
import com.breece.trackrejoice.user.api.model.UserDetails;
import com.breece.trackrejoice.user.api.model.UserProfile;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

class PlaceOrderTest {
    TestFixture testFixture = TestFixture.create().givenCommands("/com/breece/trackrejoice/classifiedsad/model/create-classifieds-ad.json");

    PlaceOrder order1 = new PlaceOrder(new OrderId("1"), new OrderDetails(
            new ClassifiedsAdId("1"), new PaymentId("1"), Instant.now(), Duration.ofDays(90)));
    PlaceOrder order2 = new PlaceOrder(new OrderId("2"), new OrderDetails(
            new ClassifiedsAdId("1"), new PaymentId("1"), Instant.now(), Duration.ofDays(90)));

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
}