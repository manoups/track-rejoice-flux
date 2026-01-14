package com.breece.coreapi.user;

import com.breece.coreapi.user.api.CreateUser;
import com.breece.coreapi.user.api.GetUsers;
import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserDetails;
import com.breece.coreapi.user.api.model.UserProfile;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandleGet;
import io.fluxzero.sdk.web.HandlePost;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersEndpoint {
    @HandlePost("/users")
    UserId createUser(UserDetails details) {
        var userId = new UserId();
        Fluxzero.sendCommandAndWait(new CreateUser(userId, details, null));
        return userId;
    }

    @HandleGet("/users")
    List<UserProfile> getUsers() {
        return Fluxzero.queryAndWait(new GetUsers());
    }

}
