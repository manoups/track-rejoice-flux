package com.breece.app.web;

import com.breece.app.web.api.UiUpdate;
import com.breece.content.api.model.Content;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserProfile;
import com.breece.order.api.order.model.Order;
import com.breece.sighting.api.model.Sighting;
import com.fasterxml.jackson.databind.JsonNode;
import io.fluxzero.common.serialization.JsonUtils;
import io.fluxzero.sdk.Fluxzero;
import com.flipkart.zjsonpatch.JsonDiff;
import io.fluxzero.sdk.common.Message;
import io.fluxzero.sdk.common.serialization.Serializer;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.tracking.handling.HandleNotification;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import io.fluxzero.sdk.web.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SocketEndpoint
@Path("/api/updates")
@Slf4j
public record UiUpdater(SocketSession session) {

    @HandleNotification
    void handleUserUpdate(Entity<UserProfile> entity) {
        session.sendMessage(entity);
    }

    @HandleNotification
    void handleIncidentUpdate(Entity<Content> entity) {
        session.sendMessage(entity);
    }

    @HandleNotification
    void handleSightingUpdate(Entity<Sighting> entity) {
        session.sendMessage(entity);
    }

    @HandleNotification
    void handleOrderUpdate(Entity<Order> entity) {session.sendMessage(entity);}

    @HandleSocketOpen
    @RequiresUser
    static UiUpdater startListening(Sender user, SocketSession session) {
        return new UiUpdater(session);
    }

}
