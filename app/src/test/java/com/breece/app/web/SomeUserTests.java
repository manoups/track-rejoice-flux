package com.breece.app.web;

import com.breece.content.api.model.Content;
import com.breece.coreapi.authentication.AuthenticationUtils;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.web.HttpRequestMethod;
import io.fluxzero.sdk.web.WebRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SomeUserTests {
    private final TestFixture testFixture = TestFixture.create();

    @BeforeEach
    void setUp() {
        testFixture.registerHandlers(ContentEndpoint.class, UiUpdater.class)
                .givenCommands("../user/create-user.json");
    }

    @Test
    @Disabled
    void sendWholeCatalogOnOpen() {
        testFixture.withHeader("Authorization", createAuthorizationHeader("viewer"))
                .whenWebRequest(openSocket())
                .expectWebResponse(r -> r.getPayload() instanceof List<?> gamesList
                        && gamesList.size() == 20);
    }

    @Test
    void sendAdditionalGamesWhenRegistered() {
        testFixture.withHeader("Authorization", createAuthorizationHeader("viewer"))
                .givenWebRequest(openSocket())
                .whenCommand("../content/create-content.json")
                .expectWebResponse(r -> r.getPayload() instanceof Entity<?> contentEntity
                        && contentEntity.isPresent() && contentEntity.get() instanceof Content && ((Content) contentEntity.get()).contentId().toString().equals("content-1"));
    }

    String createAuthorizationHeader(String user) {
        return testFixture.getFluxzero().apply(
                fc -> AuthenticationUtils.createAuthorizationHeader(new UserId(user)));
    }

    static WebRequest openSocket() {
        return WebRequest.builder().url("/api/updates").method(HttpRequestMethod.WS_OPEN).build();
    }
}
