package com.breece.trackrejoice.user;

import com.breece.trackrejoice.user.api.CreateUser;
import com.breece.trackrejoice.user.api.GetUsers;
import com.breece.trackrejoice.user.api.UserId;
import com.breece.trackrejoice.user.api.model.UserProfile;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

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
                .expectResult(r -> r.size() == 1);
    }

    @Nested
    class UsersEndpointTests {

        @BeforeEach
        void setUp() {
            testFixture.registerHandlers(new UsersEndpoint());
        }

        @Test
        void createUser() {
            testFixture.whenPost("/users", "/com/breece/trackrejoice/user/create-user-request.json")
                    .expectResult(UserId.class)
                    .expectEvents(CreateUser.class);
        }

        @Test
        void getUsers() {
            testFixture.givenPost("/users", "/com/breece/trackrejoice/user/create-user-request.json")
                    .whenGet("/users")
                    .<List<UserProfile>>expectResult(r -> r.size() == 1);
        }
    }
}