package com.breece.app;

import com.breece.app.web.PspCallbackEndpoint;
import com.breece.content.command.api.ContentState;
import com.breece.order.api.order.OrderFulfillment;
import com.breece.order.api.command.CreateOrder;
import com.breece.order.api.command.CreateOrderRemote;
import io.fluxzero.common.FileUtils;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.WebResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PaypalOrderEndpointTests {
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
    }

    TestFixture testFixture = TestFixture.create(ContentState.class, new PspCallbackEndpoint(), new OrderFulfillment(), new PaypalOrderTests.EndpointMock()).withProperty("pgp", "paypal").withProperty("paypal.url", "https://paypal-value");

    @Disabled
    @Test
    void sendOrderEndpoint() {
        testFixture.
                givenCommands("/com/breece/sighting/ui/content/create-content.json")
                .whenPost("/payments/paypal/orders/content-1")
                .expectNoErrors()
                .expectEvents(CreateOrder.class)
                .expectOnlyCommands(CreateOrderRemote.class);
    }
}
