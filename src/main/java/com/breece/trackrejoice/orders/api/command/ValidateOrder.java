package com.breece.trackrejoice.orders.api.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.common.SendWebRequestWithMetadata;
import com.breece.trackrejoice.orders.api.model.OrderId;
import com.fasterxml.jackson.databind.JsonNode;
import com.paypal.sdk.models.*;
import io.fluxzero.common.api.Metadata;
import io.fluxzero.common.serialization.JsonUtils;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.TrackSelf;
import io.fluxzero.sdk.web.HttpRequestMethod;
import io.fluxzero.sdk.web.WebRequest;
import io.fluxzero.sdk.web.WebResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

import static io.fluxzero.sdk.configuration.ApplicationProperties.requireProperty;

@Value
@TrackSelf
@Consumer(name = "validate-order-consumer")
public class ValidateOrder extends SendWebRequestWithMetadata {
    String proxyConsumer = "proxy-payments";

    @NotNull
    OrderId orderId;

    @NotNull
    String reference;


    @Override
    protected WebRequest.Builder buildRequest(WebRequest.Builder requestBuilder, Sender sender, Metadata metadata) {
        CreateOrderInput ordersCreateInput = new CreateOrderInput.Builder(
                null,
                new OrderRequest.Builder(
                        CheckoutPaymentIntent.fromString("CAPTURE"),
                        List.of(
                                new PurchaseUnitRequest.Builder(
                                        new AmountWithBreakdown.Builder(
                                                "EUR",
                                                "5"
                                        ).build()
                                ).build()
                        )
                ).build()
        ).build();
        return requestBuilder
                .url("%s/v2/checkout/orders".formatted(requireProperty("paypal.url")))
                .method(HttpRequestMethod.POST)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer %s".formatted(metadata.get("token")))
                .body(ordersCreateInput.getBody());
    }

    @Override
    protected String handleResponse(WebResponse response, WebRequest request, Metadata metadata) {
        String stringResult = (String) super.handleResponse(response, request);
        JsonNode result = JsonUtils.fromJson(stringResult, JsonNode.class);
        Fluxzero.sendAndForgetCommand(new UpdateOrder(orderId, result.get("status").asText()));
        return result.get("id").asText();
    }
}
