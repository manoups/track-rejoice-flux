package com.breece.order;

import com.breece.content.command.api.ContentState;
import com.breece.order.api.command.CreateOrderRemoteSuccess;
import com.breece.order.api.command.DeleteOrder;
import com.breece.order.api.order.OrderErrors;
import com.breece.order.api.order.OrderFulfillment;
import com.breece.order.api.command.CreateOrderRemote;
import com.breece.order.api.order.OrderHandler;
import com.breece.order.api.payment.OrderProcess;
import com.breece.util.MockQueryHandler;
import com.paypal.sdk.models.OrderRequest;
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
    final TestFixture testFixture = TestFixture.create(new OrderFulfillment(), OrderProcess.class, new OrderHandler(), new MockQueryHandler(), ContentState.class, new EndpointMock()).withClock(Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.systemDefault()))
            .givenCommands("../content/create-content.json").withProperty("paypal.url", "https://paypal-value");


    @Test
    void placeOrder() {
        testFixture.whenCommand("place-order.json")
                .expectOnlyCommands(CreateOrderRemote.class)
                .expectOnlyWebRequests(String.class, OrderRequest.class)
                .expectOnlyEvents("place-order.json", CreateOrderRemoteSuccess.class);
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

    @Test
    void whenContentDeleted_thenCreatedOrdersAlsoDeleted() {
        testFixture.givenCommands("place-order.json")
                .whenCommand("../content/delete-content.json")
                .expectOnlyEvents("../content/delete-content.json", DeleteOrder.class)
                .expectNoWebRequests()
                .expectOnlyCommands(DeleteOrder.class);
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