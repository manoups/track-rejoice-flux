package com.breece.payment;

import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentState;
import com.breece.coreapi.common.PaypalAuthenticate;
import com.breece.order.api.command.CreateOrder;
import com.breece.order.api.command.UpdateOrder;
import com.breece.order.api.order.OrderFulfillment;
import com.breece.order.api.order.model.OrderDetails;
import com.breece.order.api.order.model.OrderId;
import com.breece.service.api.model.ServiceId;
import com.breece.util.MockQueryHandler;
import io.fluxzero.common.DefaultMemoizingSupplier;
import io.fluxzero.common.FileUtils;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.WebResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

import static io.fluxzero.common.ObjectUtils.memoize;
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


        TestFixture testFixture = TestFixture.create(ContentState.class, new OrderFulfillment(),
                new MockQueryHandler(), new EndpointMock()).withProperty("pgp", "paypal")
                .withProperty("paypal.url", "https://paypal-value");

        @Test
        void paypalAuthenticate() {
            testFixture.whenQuery(new PaypalAuthenticate())
                    .expectNoErrors()
                    .expectResult("A21AALDt0z1oWeRUxAALcnBjR6qjIq6Vs1mWnMKzrdvs59Sj6jMu8f_H09IxiV9XQSZZ9RP_cOQdBVHWcWQjo0ZYKwqqBLBAw"::equals);
        }

        @Disabled
        @Test
        public void givenMemoizedSupplier_whenGet_thenSubsequentGetsAreFast() {
            Supplier<String> memoizedSupplier = memoize(new DefaultMemoizingSupplier<>(this::authenticate, Duration.ofSeconds(30), testFixture.getClock()));
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
            CreateOrder createOrder = new CreateOrder(new OrderId("1"), new ContentId("1"), new OrderDetails(listOf(new ServiceId("1")), Instant.now()), UUID.randomUUID().toString());
            testFixture.givenCommands("../content/create-content.json").whenCommand(createOrder)
                    .expectEvents(createOrder)
                    .expectCommands(UpdateOrder.class);
        }
    }

    @Nested
    class PaypalOrderFailTests {
        static class EndpointMock {
            @HandlePost("https://paypal-value/v1/oauth2/token")
            WebResponse authenticationToken() {
                return WebResponse.builder()
                        .status(401)
                        .payload(FileUtils.loadFile("..//common/auth-response-error.json"))
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