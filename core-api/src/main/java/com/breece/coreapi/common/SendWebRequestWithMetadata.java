package com.breece.coreapi.common;

import com.breece.coreapi.authentication.Sender;
import io.fluxzero.common.api.Metadata;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.web.WebRequest;
import io.fluxzero.sdk.web.WebResponse;

public abstract class SendWebRequestWithMetadata extends SendWebRequest {
    protected abstract WebRequest.Builder buildRequest(WebRequest.Builder requestBuilder, Sender sender, Metadata metadata);
    protected abstract Object handleResponse(WebResponse response, WebRequest request, Metadata metadata);

    @HandleQuery
    @HandleCommand
    public Object handle(Sender sender, Metadata metadata) {
        WebRequest request = buildRequest(WebRequest.builder(), sender, metadata).build()
                .addMetadata("$thread", Thread.currentThread().getName());
        WebResponse webResponse = Fluxzero.get().webRequestGateway().sendAndWait(request, requestSettings());
        return handleResponse(webResponse, request, metadata);
    }
}
