package com.breece.order.api.command;

import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SendWebRequestWithMetadata;
import com.breece.order.api.order.model.OrderId;
import com.breece.order.api.payment.OrderStatus;
import com.breece.service.api.model.ServiceDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.paypal.sdk.models.*;
import io.fluxzero.common.api.Metadata;
import io.fluxzero.common.serialization.JsonUtils;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.ForeverRetryingErrorHandler;
import io.fluxzero.sdk.tracking.TrackSelf;
import io.fluxzero.sdk.web.HttpRequestMethod;
import io.fluxzero.sdk.web.WebRequest;
import io.fluxzero.sdk.web.WebResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

import static io.fluxzero.sdk.configuration.ApplicationProperties.requireProperty;

@Value
@TrackSelf
@Consumer(name = "validate-order-consumer", errorHandler = ForeverRetryingErrorHandler.class)
@Accessors(fluent = true)
public class CreateOrderRemote extends SendWebRequestWithMetadata {
    private static final String CURRENCY_CODE = "EUR";
    String proxyConsumer = "proxy-payments";

    @NotNull
    OrderId orderId;

    @NotNull
    String pspReference;

    @NotNull
    List<ServiceDetails> serviceDetails;


    @Override
    protected WebRequest.Builder buildRequest(WebRequest.Builder requestBuilder, Sender sender, Metadata metadata) {
        CreateOrderInput ordersCreateInput = new CreateOrderInput.Builder(
                null,
                new OrderRequest.Builder(
                        CheckoutPaymentIntent.fromString("CAPTURE"),
                        List.of(
                                new PurchaseUnitRequest.Builder(
                                        new AmountWithBreakdown.Builder(
                                                CURRENCY_CODE,
                                                serviceDetails.stream().map(ServiceDetails::price).reduce(BigDecimal.ZERO, BigDecimal::add).toString()
                                        ).build()
                                ).items(serviceDetails.stream().map(it -> new Item.Builder(it.name(),
                                                new Money.Builder(CURRENCY_CODE, it.price().toString()).build(), "1").build()).toList())
                                        .build()
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
        Fluxzero.loadAggregate(orderId).apply(new CreateOrderRemoteSuccess(orderId, pspReference, OrderStatus.valueOf(result.get("status").asText())));
        return result.get("id").asText();
    }
}
