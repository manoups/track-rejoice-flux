package com.breece.trackrejoice.orders.api.model;

import com.breece.trackrejoice.orders.api.OrderErrors;
import com.breece.trackrejoice.orders.api.OrderFulfillment;
import com.breece.trackrejoice.payments.api.PaymentAccepted;
import com.breece.trackrejoice.payments.api.ValidatePayment;
import io.fluxzero.common.serialization.JsonUtils;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.web.HandlePost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;

class OrderTest {
    final TestFixture testFixture = TestFixture.create(OrderFulfillment.class).withClock(Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.systemDefault()));

    @BeforeEach
    void setUp() {
        testFixture.registerHandlers(new ExternalsMock());
    }

    @Test
    void placeOrder() {
        testFixture.whenCommand("/order/place-order.json")
                .expectEvents("/order/place-order.json")
                .expectCommands(ValidatePayment.class)
                .expectEvents(PaymentAccepted.class);
    }

    @Test
    void abortOrder() {
        testFixture.givenCommands("/order/place-order.json")
                .whenCommand("/order/abort-order.json")
                .expectEvents("/order/abort-order.json");
    }

    @Test
    void abortOrderAfter24Hours() {
        testFixture.givenCommands("/order/place-order.json")
                .whenTimeElapses(Duration.ofHours(24).plus(1, ChronoUnit.SECONDS))
                .andThen()
                .whenCommand("/order/abort-order.json")
                .expectError(OrderErrors.tooOld);
    }

    static class ExternalsMock {
        @HandlePost("/psp/validate/{paymentId}")
        String validate(String paymentId) {
            return JsonUtils.asJson(Map.of("accepted", true));
        }
    }
}