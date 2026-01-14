package com.breece.coreapi.common;

import com.breece.coreapi.authentication.Sender;
import com.fasterxml.jackson.databind.JsonNode;
import io.fluxzero.common.serialization.JsonUtils;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.ForeverRetryingErrorHandler;
import io.fluxzero.sdk.tracking.TrackSelf;
import io.fluxzero.sdk.tracking.handling.Request;
import io.fluxzero.sdk.web.HttpRequestMethod;
import io.fluxzero.sdk.web.WebRequest;
import io.fluxzero.sdk.web.WebResponse;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import static io.fluxzero.sdk.configuration.ApplicationProperties.getProperty;
import static io.fluxzero.sdk.configuration.ApplicationProperties.requireProperty;

@Slf4j
@Value
@TrackSelf
@Consumer(name = "auth-consumer", errorHandler = ForeverRetryingErrorHandler.class)
public class PaypalAuthenticate extends SendWebRequestWithoutMetadata implements Request<String> {

    @Override
    protected WebRequest.Builder buildRequest(WebRequest.Builder requestBuilder, Sender sender) {
        WebRequest.Builder authorization = requestBuilder
                .url("%s/v1/oauth2/token".formatted(requireProperty("paypal.url")))
                .method(HttpRequestMethod.POST)
                .acceptGzipEncoding(false)
                .header("Authorization", "Basic %s".formatted(getProperty("paypal.basic.auth.user")))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body("grant_type=client_credentials");
        log.info(authorization.toString());
        return authorization;
    }

    @Override
    protected String handleResponse(WebResponse response, WebRequest request) {
        String stringResult = (String) super.handleResponse(response, request);
        JsonNode result = JsonUtils.fromJson(stringResult, JsonNode.class);
        return result.get("access_token").asText();
    }
}
