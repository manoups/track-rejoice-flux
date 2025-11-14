package com.breece.trackrejoice.common;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import com.breece.trackrejoice.orders.api.OrderFulfillment;
import com.breece.trackrejoice.orders.api.command.PlaceOrder;
import com.breece.trackrejoice.orders.api.command.UpdateOrder;
import com.breece.trackrejoice.orders.api.command.ValidateOrder;
import com.breece.trackrejoice.orders.api.model.OrderDetails;
import com.breece.trackrejoice.orders.api.model.OrderId;
import com.breece.trackrejoice.payments.api.PayPalEndpoint;
import com.breece.trackrejoice.payments.api.model.PaymentId;
import io.fluxzero.common.FileUtils;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.WebResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

class PaypalAuthenticateTest {
    @Nested
    class PaypalOrderTests {
        static class EndpointMock {
            @HandlePost("https://api-m.sandbox.paypal.com/v1/oauth2/token")
            WebResponse authenticationToken() {
                return WebResponse.builder()
                        .payload(FileUtils.loadFile("/common/auth-response.json"))
                        .build();
            }

            @HandlePost("https://api-m.sandbox.paypal.com/v2/checkout/orders")
            WebResponse placeOrder() {
                return WebResponse.builder()
                        .payload(FileUtils.loadFile("/common/checkout-order.json"))
                        .build();
            }
        }


        TestFixture testFixture = TestFixture.create(new OrderFulfillment(), new EndpointMock()).withProperty("pgp", "paypal");

        @Test
        void paypalAuthenticate() {
            testFixture.whenQuery(new PaypalAuthenticate())
                    .expectNoErrors()
                    .expectResult("A21AALDt0z1oWeRUxAALcnBjR6qjIq6Vs1mWnMKzrdvs59Sj6jMu8f_H09IxiV9XQSZZ9RP_cOQdBVHWcWQjo0ZYKwqqBLBAw"::equals);
        }

        @Test
        void sendOrder() {
            PlaceOrder order1 = new PlaceOrder(new OrderId("1"), new OrderDetails(
                    new ClassifiedsAdId("1"), new PaymentId("1"), Instant.now(), Duration.ofDays(90)));
            testFixture.whenCommand(order1)
                    .expectEvents(PlaceOrder.class)
                    .expectCommands(ValidateOrder.class, UpdateOrder.class);
        }

        @Nested
        class PaypalOrderEndpointTests {

            @BeforeEach
            void setUp() {
                testFixture.registerHandlers(new PayPalEndpoint());
            }

            @Test
            void sendOrderEndpoint() {
                testFixture.
                        givenCommands("/classifiedsad/create-classifieds-ad.json")
                        .whenPost("/payments/paypal/orders/ad-1")
                        .expectEvents(PlaceOrder.class)
                        .expectCommands(ValidateOrder.class, UpdateOrder.class);
            }
        }
    }

    @Nested
    class PaypalOrderFailTests {
        static class EndpointMock {
            @HandlePost("https://api-m.sandbox.paypal.com/v1/oauth2/token")
            WebResponse authenticationToken() {
                return WebResponse.builder()
                        .status(401)
                        .payload(FileUtils.loadFile("/common/auth-response-error.json"))
                        .build();
            }
        }

        TestFixture testFixture = TestFixture.create(new OrderFulfillment(), new EndpointMock());

        @Test
        void paypalAuthenticate() {
            testFixture.whenQuery(new PaypalAuthenticate())
                    .expectError(UnauthorizedException.class);
        }
    }
}