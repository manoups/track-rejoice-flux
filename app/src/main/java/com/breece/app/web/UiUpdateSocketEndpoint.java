package com.breece.app.web;

import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import io.fluxzero.sdk.web.HandleSocketClose;
import io.fluxzero.sdk.web.HandleSocketOpen;
import io.fluxzero.sdk.web.SocketEndpoint;
import io.fluxzero.sdk.web.SocketSession;
import org.springframework.beans.factory.annotation.Autowired;

@SocketEndpoint
public class UiUpdateSocketEndpoint {
    @HandleSocketOpen("/api/updates")
    @RequiresUser
    void startListening(Sender user, SocketSession session, @Autowired UiUpdater uiUpdater) {
        uiUpdater.registerSession(user.userId(), session);
    }

    @HandleSocketClose("/api/updates")
    void stopListening(SocketSession session, @Autowired UiUpdater uiUpdater) {
        uiUpdater.unregisterSession(session);
    }
}
