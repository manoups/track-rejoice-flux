package com.breece.app;

import com.breece.app.web.SightingEndpoint;
import com.breece.coreapi.authentication.AuthenticationUtils;
import com.breece.coreapi.user.api.UserId;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.command.api.DeleteSighting;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;

public class SightingEndpointTests {
    final TestFixture testFixture = TestFixture.create(new SightingEndpoint())
            .givenCommands("user/create-user.json");

    @Test
    void createSighting() {
        testFixture
                .withHeader("Authorization", createAuthorizationHeader("viewer"))
                .whenPost("api/sighting", "/com/breece/app/sighting/create-sighting.json")
                .expectResult(SightingId.class).expectEvents(CreateSighting.class);
    }

    @Test
    void getSightings() {
        testFixture
                .withHeader("Authorization", createAuthorizationHeader("viewer"))
                .givenPost("api/sighting", "/com/breece/app/sighting/create-sighting.json")
                .whenGet("api/sighting?page=0&page-size=10")
                .expectResult(hasSize(1));
    }

    @Test
    void deleteSighting() {
        testFixture
                .withHeader("Authorization", createAuthorizationHeader("viewer"))
                .givenCommandsByUser("viewer", "/com/breece/app/sighting/create-sighting-command.json")
                .whenDelete("api/sighting/1")
                .expectResult(SightingId.class).expectOnlyEvents(DeleteSighting.class);
    }

    String createAuthorizationHeader(String user) {
        return testFixture.getFluxzero().apply(
                fc -> AuthenticationUtils.createAuthorizationHeader(new UserId(user)));
    }
}
