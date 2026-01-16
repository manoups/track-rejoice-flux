package com.breece.sighting.ui.common;

import com.breece.content.command.api.ContentState;
import com.breece.coreapi.common.PaypalAuthenticate;
import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.order.model.OrderDetails;
import com.breece.coreapi.order.model.OrderId;
import com.breece.coreapi.service.model.ServiceId;
import com.breece.order.api.OrderFulfillment;
import com.breece.order.api.command.PlaceOrder;
import com.breece.order.api.command.UpdateOrder;
import com.breece.order.api.command.ValidateOrder;
import com.breece.sighting.ui.PspCallbackEndpoint;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import io.fluxzero.common.FileUtils;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.WebResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hibernate.internal.util.collections.CollectionHelper.listOf;

class PaypalAuthenticateTest {
    @Nested
    class PaypalOrderTests {
        static class EndpointMock {
            @HandlePost("https://paypal-value/v1/oauth2/token")
            WebResponse authenticationToken() {
                /*try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {}*/
                return WebResponse.builder()
                        .payload(FileUtils.loadFile("/com/breece/sighting/ui/common/auth-response.json"))
                        .build();
            }

            @HandlePost("https://paypal-value/v2/checkout/orders")
            WebResponse placeOrder() {
                return WebResponse.builder()
                        .payload(FileUtils.loadFile("/com/breece/sighting/ui/common/checkout-order.json"))
                        .build();
            }
        }


        TestFixture testFixture = TestFixture.create(ContentState.class, new OrderFulfillment(), new EndpointMock()).withProperty("pgp", "paypal").withProperty("paypal.url", "https://paypal-value").givenCommands("/com/breece/sighting/ui/service/create-service.json");

        @Test
        void paypalAuthenticate() {
            testFixture.whenQuery(new PaypalAuthenticate())
                    .expectNoErrors()
                    .expectResult("A21AALDt0z1oWeRUxAALcnBjR6qjIq6Vs1mWnMKzrdvs59Sj6jMu8f_H09IxiV9XQSZZ9RP_cOQdBVHWcWQjo0ZYKwqqBLBAw"::equals);
        }

        @Disabled
        @Test
        public void givenMemoizedSupplier_whenGet_thenSubsequentGetsAreFast() {
            Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(this::authenticate, 30, TimeUnit.SECONDS);
            String expectedValue = "A21AALDt0z1oWeRUxAALcnBjR6qjIq6Vs1mWnMKzrdvs59Sj6jMu8f_H09IxiV9XQSZZ9RP_cOQdBVHWcWQjo0ZYKwqqBLBAw";
            assertSupplierGetExecutionResultAndDuration(
                    memoizedSupplier, expectedValue, 2000D);
            assertSupplierGetExecutionResultAndDuration(
                    memoizedSupplier, expectedValue, 0D);
            assertSupplierGetExecutionResultAndDuration(
                    memoizedSupplier, expectedValue, 0D);
        }

        private <T> void assertSupplierGetExecutionResultAndDuration(
                Supplier<T> supplier, T expectedValue, double expectedDurationInMs) {
            Instant start = Instant.now();
            T value = supplier.get();
            Long durationInMs = Duration.between(start, Instant.now()).toMillis();
            double marginOfErrorInMs = 100D;

            assertThat(value, is(equalTo(expectedValue)));
            assertThat(
                    durationInMs.doubleValue(),
                    is(closeTo(expectedDurationInMs, marginOfErrorInMs)));
        }

        String authenticate() {
            return Fluxzero.queryAndWait(new PaypalAuthenticate());
        }

        @Test
        void sendOrder() {
            PlaceOrder order1 = new PlaceOrder(new OrderId("1"), new ContentId("1"), new OrderDetails(listOf(new ServiceId("1")), Instant.now(), Duration.ofDays(90)));
            testFixture.givenCommands("/com/breece/sighting/ui/content/create-content.json").whenCommand(order1)
                    .expectEvents(PlaceOrder.class)
                    .expectCommands(ValidateOrder.class, UpdateOrder.class);
        }

        @Nested
        class PaypalOrderEndpointTests {

            @BeforeEach
            void setUp() {
                testFixture.registerHandlers(new PspCallbackEndpoint());
            }

            @Disabled
            @Test
            void sendOrderEndpoint() {
                testFixture.
                        givenCommands("/com/breece/sighting/ui/content/create-content.json")
                        .whenPost("/payments/paypal/orders/content-1")
                        .expectNoErrors()
                        .expectEvents(PlaceOrder.class)
                        .expectCommands(ValidateOrder.class, UpdateOrder.class);
            }
        }
    }

    @Nested
    class PaypalOrderFailTests {
        static class EndpointMock {
            @HandlePost("https://paypal-value/v1/oauth2/token")
            WebResponse authenticationToken() {
                return WebResponse.builder()
                        .status(401)
                        .payload(FileUtils.loadFile("/com/breece/sighting/ui/common/auth-response-error.json"))
                        .build();
            }
        }

        TestFixture testFixture = TestFixture.create(new OrderFulfillment(), new EndpointMock()).withProperty("paypal.url", "https://paypal-value");

        @Test
        void paypalAuthenticate() {
            testFixture.whenQuery(new PaypalAuthenticate())
                    .expectError(UnauthorizedException.class);
        }
    }
}