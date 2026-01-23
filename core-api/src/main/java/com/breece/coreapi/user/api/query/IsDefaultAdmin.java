package com.breece.coreapi.user.api.query;

import com.breece.coreapi.authentication.RequiresRole;
import com.breece.coreapi.authentication.Role;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.getProperty;

@RequiresRole(Role.ADMIN)
public record IsDefaultAdmin(@NotNull @Valid String email) {
    static List<String> defaultAdmins() {
        return Fluxzero.get().cache().computeIfAbsent("IsDefaultAdmin", _ -> Arrays.stream(
                        getProperty("default-admins", "").split(","))
                .map(String::trim).filter(s -> !s.isBlank()).map(String::new).toList());
    }

    @HandleQuery
    boolean handle() {
        return defaultAdmins().contains(email);
    }
}
