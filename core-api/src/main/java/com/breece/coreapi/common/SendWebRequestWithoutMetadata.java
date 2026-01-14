package com.breece.coreapi.common;

import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.web.WebRequest;
import io.fluxzero.sdk.web.WebResponse;

public abstract class SendWebRequestWithoutMetadata extends SendWebRequest {
    protected abstract WebRequest.Builder buildRequest(WebRequest.Builder requestBuilder, Sender sender);

    @HandleQuery
    @HandleCommand
    public Object handle(Sender sender) {
        WebRequest request = buildRequest(WebRequest.builder(), sender).build()
                .addMetadata("$thread", Thread.currentThread().getName());
        WebResponse webResponse = Fluxzero.get().webRequestGateway().sendAndWait(request, requestSettings());
        return handleResponse(webResponse, request);
    }
}
