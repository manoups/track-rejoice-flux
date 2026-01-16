package com.breece.payment;

import com.breece.payment.api.ExecutePayment;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.*;

public class PaymentTest {
    final TestFixture testFixture = TestFixture.create();

    @BeforeEach
    void setUp() {
        testFixture.registerHandlers().givenCommands("create-content.json");
    }

    @Disabled
    @Timeout(15)
    @Test
    void createContentDetails() {
        testFixture.whenEvent("payment-initiated.json")
                .expectEvents(ExecutePayment.class);
    }
}
