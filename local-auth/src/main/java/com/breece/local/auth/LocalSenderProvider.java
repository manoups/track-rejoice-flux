package com.breece.local.auth;

import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.user.api.model.UserProfile;
import io.fluxzero.common.MessageType;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.common.HasMessage;
import io.fluxzero.sdk.common.serialization.DeserializingMessage;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.tracking.handling.authentication.AbstractUserProvider;
import io.fluxzero.sdk.tracking.handling.authentication.User;

import static java.lang.System.getProperty;

public class LocalSenderProvider extends AbstractUserProvider {

    public LocalSenderProvider() {
        super(Sender.class);
    }

    @Override
    public User fromMessage(HasMessage message) {
        if(getProperty("trackrejoice.auth") == null) {
            return null;
        }
            if (message instanceof DeserializingMessage dm && dm.getMessageType() == MessageType.WEBREQUEST) {
                return getSystemUser();
            }
        return super.fromMessage(message);
    }

    @Override
    public User getUserById(Object userId) {
        return Fluxzero.loadAggregate(userId, UserProfile.class)
                .mapIfPresent(Entity::get)
                .map( userProfile -> Sender.builder().userId(userProfile.userId()).userRole(userProfile.role()).build())
                .orElse(null);
    }

    @Override
    public User getSystemUser() {
        return Sender.system;
    }
}