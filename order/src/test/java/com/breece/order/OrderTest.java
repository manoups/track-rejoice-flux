package com.breece.order;

import com.breece.content.command.api.ContentState;
import com.breece.order.api.order.OrderErrors;
import com.breece.order.api.order.OrderFulfillment;
import com.breece.order.api.command.UpdateOrder;
import com.breece.order.api.command.CreateOrderRemote;
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
    final TestFixture testFixture = TestFixture.create(new OrderFulfillment(), ContentState.class, new EndpointMock()).withClock(Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.systemDefault()))
            .givenCommands("../service/create-service.json", "../content/create-content.json").withProperty("paypal.url", "https://paypal-value");


    @Test
    void placeOrder() {
        testFixture.whenCommand("place-order.json")
                .expectCommands(CreateOrderRemote.class, UpdateOrder.class)
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
        @HandlePost("https://paypal-value/v1/oauth2/token")
        WebResponse authenticationToken() {
            return WebResponse.builder()
                    .payload(FileUtils.loadFile("../common/auth-response.json"))
                    .build();
        }

        @HandlePost("https://paypal-value/v2/checkout/orders")
        WebResponse placeOrder() {
            return WebResponse.builder()
                    .payload(FileUtils.loadFile("../common/checkout-order.json"))
                    .build();
        }
    }
}