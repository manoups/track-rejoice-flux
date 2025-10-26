package com.breece.trackrejoice.orders.api.model;

import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

class OrderTest {
    final TestFixture testFixture = TestFixture.create();

    @Test
    void placeOrder() {
        testFixture.whenCommand("/order/place-order.json")
                .expectEvents("/order/place-order.json");
    }

    @Test
    void abortOrder() {
        testFixture.givenCommands("/order/place-order.json")
                .whenCommand("/order/abort-order.json")
                .expectEvents("/order/abort-order.json");
    }
}