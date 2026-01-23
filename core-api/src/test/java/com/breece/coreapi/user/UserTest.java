package com.breece.coreapi.user;

import com.breece.coreapi.user.api.GetUsers;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;

class UserTest {
    final TestFixture testFixture = TestFixture.create();

    @Test
    void createViewer() {
        testFixture.whenCommand("create-user.json")
                .expectEvents("create-user.json");
    }


    @Test
    void createUser() {
        testFixture
                .whenCommand("create-user.json")
                .expectEvents("create-user.json");
    }

    @Test
    void createUserNotAllowedForNonAdmin() {
        testFixture.whenCommandByUser("viewer", "create-admin.json")
                .expectExceptionalResult(UnauthorizedException.class);
    }

    @Test
    void setRole() {
        testFixture
                .givenCommands("create-user.json")
                .whenCommand("make-admin.json")
                .expectEvents("make-admin.json");
    }

    @Test
    void setRoleAsNonAdminNotAllowed() {
        testFixture
                .givenCommands("create-user.json")
                .whenCommandByUser("viewer", "make-admin.json")
                .expectExceptionalResult(UnauthorizedException.class);
    }

    @Test
    void getUsers() {
        testFixture.givenCommands("create-user.json")
                .whenQuery(new GetUsers())
                .expectResult(hasSize(1));
    }
}
