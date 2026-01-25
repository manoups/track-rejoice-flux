package com.breece.coreapi.user;

import com.breece.coreapi.user.api.UserId;
import jakarta.validation.constraints.NotNull;

public interface WithOwner {
    @NotNull
    UserId ownerId();
}
