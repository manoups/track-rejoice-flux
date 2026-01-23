package com.breece.app.web.support;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public final class TestSocketListener implements WebSocket.Listener {
    private final CompletableFuture<Void> openFuture;
    private final CompletableFuture<String> messageFuture;
    private final StringBuilder buffer = new StringBuilder();

    public TestSocketListener(CompletableFuture<Void> openFuture, CompletableFuture<String> messageFuture) {
        this.openFuture = openFuture;
        this.messageFuture = messageFuture;
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        openFuture.complete(null);
        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        if (!openFuture.isDone()) {
            openFuture.completeExceptionally(new IllegalStateException("Closed before open: " + reason));
        }
        if (!messageFuture.isDone()) {
            messageFuture.completeExceptionally(new IllegalStateException("Closed: " + reason));
        }
        return null;
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        buffer.append(data);
        if (last && !messageFuture.isDone()) {
            messageFuture.complete(buffer.toString());
        }
        webSocket.request(1);
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        if (!openFuture.isDone()) {
            openFuture.completeExceptionally(error);
        }
        messageFuture.completeExceptionally(error);
    }
}
