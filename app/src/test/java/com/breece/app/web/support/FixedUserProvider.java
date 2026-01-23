package com.breece.app.web.support;

import com.breece.coreapi.authentication.Role;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.common.MessageType;
import io.fluxzero.sdk.common.HasMessage;
import io.fluxzero.sdk.common.serialization.DeserializingMessage;
import io.fluxzero.sdk.tracking.handling.authentication.AbstractUserProvider;
import io.fluxzero.sdk.tracking.handling.authentication.User;

public final class FixedUserProvider extends AbstractUserProvider {
    private final Sender webUser = Sender.builder()
            .userId(new UserId("viewer"))
            .userRole(Role.MANAGER)
            .build();
    private final Sender systemUser = Sender.builder()
            .userId(new UserId("admin"))
            .userRole(Role.ADMIN)
            .build();

    public FixedUserProvider() {
        super(Sender.class);
    }

    @Override
    public User fromMessage(HasMessage message) {
        if (message instanceof DeserializingMessage dm && dm.getMessageType() == MessageType.WEBREQUEST) {
            return webUser;
        }
        return super.fromMessage(message);
    }

    @Override
    public User getUserById(Object userId) {
        if (userId instanceof UserId id) {
            return resolveUser(id.getFunctionalId());
        }
        if (userId instanceof String id) {
            return resolveUser(id);
        }
        return null;
    }

    @Override
    public User getSystemUser() {
        return systemUser;
    }

    private User resolveUser(String id) {
        if ("user".equals(id)) {
            return webUser;
        }
        if ("admin".equals(id)) {
            return systemUser;
        }
        return null;
    }
}
