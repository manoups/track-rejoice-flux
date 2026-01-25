package com.breece.app;

import com.breece.app.web.UsersEndpoint;
import com.breece.coreapi.user.api.CreateUser;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;

class UserTest {
    final TestFixture testFixture = TestFixture.create(new UsersEndpoint());

    @Test
    void createUser() {
        testFixture.whenPost("/users", "/com/breece/app/user/create-user-request.json")
                .expectResult(UserId.class)
                .expectEvents(CreateUser.class);
    }

    @Test
    void getUsers() {
        testFixture.givenPost("/users", "/com/breece/app/user/create-user-request.json")
                .whenGet("/users")
                .expectResult(hasSize(1));
    }
}