package com.breece.app.web;

import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import io.fluxzero.sdk.web.HandleSocketClose;
import io.fluxzero.sdk.web.HandleSocketOpen;
import io.fluxzero.sdk.web.SocketEndpoint;
import io.fluxzero.sdk.web.SocketSession;

@SocketEndpoint
public class UiUpdateSocketEndpoint {
    private final UiUpdater uiUpdater;

    public UiUpdateSocketEndpoint(UiUpdater uiUpdater) {
        this.uiUpdater = uiUpdater;
    }

    @HandleSocketOpen("/api/updates")
    @RequiresUser
    void startListening(Sender user, SocketSession session) {
        uiUpdater.registerSession(user.userId(), session);
    }

    @HandleSocketClose("/api/updates")
    void stopListening(SocketSession session) {
        uiUpdater.unregisterSession(session);
    }
}
