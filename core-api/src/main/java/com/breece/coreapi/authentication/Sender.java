package com.breece.coreapi.authentication;

import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.authentication.User;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Builder(toBuilder = true)
@Slf4j
public record Sender(@NonNull UserId userId, Role userRole) implements User {

    public static final Sender system = builder()
            .userId(new UserId("system")).userRole(Role.ADMIN).build();

    public static Sender getCurrent() {
        return User.getCurrent();
    }

    public static Sender createSender(UserId userId) {
        UserProfile userProfile = Fluxzero.loadAggregate(userId).get();
        if (userProfile == null) {
            log.info("User {} does not exist", userId);
            return null;
        }
        return createSender(userProfile);
    }

    public static Sender createSender(UserProfile userProfile) {
        return Sender.builder().userId(userProfile.userId()).userRole(userProfile.role())
                .build();
    }


    @Override
    @JsonIgnore
    public String getName() {
        return userId.getFunctionalId();
    }

    public boolean hasRole(Role role) {
        return role.matches(userRole);
    }

    @Override
    public boolean hasRole(String role) {
        return hasRole(Role.valueOf(role));
    }

    public boolean isAdmin() {
        return Role.ADMIN.matches(userRole);
    }

    public boolean isAuthorizedFor(UserId userId) {
        return isAdmin() || Objects.equals(this.userId, userId);
    }

    public boolean nonAuthorizedFor(UserId userId) {
        return !isAuthorizedFor(userId);
    }
}
