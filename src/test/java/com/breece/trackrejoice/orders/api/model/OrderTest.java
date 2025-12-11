package com.breece.trackrejoice.orders.api.model;

import com.breece.trackrejoice.orders.api.OrderErrors;
import com.breece.trackrejoice.orders.api.OrderFulfillment;
import com.breece.trackrejoice.orders.api.command.UpdateOrder;
import com.breece.trackrejoice.orders.api.command.ValidateOrder;
import io.fluxzero.common.FileUtils;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.WebResponse;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

class OrderTest {
    final TestFixture testFixture = TestFixture.create(new OrderFulfillment(), new EndpointMock()).withClock(Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.systemDefault()))
            .givenCommands("/com/breece/trackrejoice/content/model/create-content.json");


    @Test
    void placeOrder() {
        testFixture.whenCommand("place-order.json")
                .expectCommands(ValidateOrder.class, UpdateOrder.class)
                .expectEvents("place-order.json", UpdateOrder.class);
    }

    @Test
    void abortOrder() {
        testFixture.givenCommands("place-order.json")
                .whenCommand("abort-order.json")
                .expectEvents("abort-order.json");
    }

    @Test
    void abortOrderAfter24Hours() {
        testFixture.givenCommands("place-order.json")
                .whenTimeElapses(Duration.ofHours(24).plus(1, ChronoUnit.SECONDS))
                .andThen()
                .whenCommand("abort-order.json")
                .expectError(OrderErrors.tooOld);
    }

    static class EndpointMock {
        @HandlePost("https://api-m.sandbox.paypal.com/v1/oauth2/token")
        WebResponse authenticationToken() {
            return WebResponse.builder()
                    .payload(FileUtils.loadFile("/com/breece/trackrejoice/common/auth-response.json"))
                    .build();
        }

        @HandlePost("https://api-m.sandbox.paypal.com/v2/checkout/orders")
        WebResponse placeOrder() {
            return WebResponse.builder()
                    .payload(FileUtils.loadFile("/com/breece/trackrejoice/common/checkout-order.json"))
                    .build();
        }
    }
}