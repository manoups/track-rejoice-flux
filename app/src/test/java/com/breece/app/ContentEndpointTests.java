package com.breece.app;

import com.breece.app.web.ContentEndpoint;
import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.CreateContent;
import com.breece.coreapi.authentication.AuthenticationUtils;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;

public class ContentEndpointTests {
    final TestFixture testFixture = TestFixture.create(new ContentEndpoint(), ContentState.class)
            .givenCommands("user/create-user.json");

    @Test
    void createContent() {
        testFixture
                .withHeader("Authorization", createAuthorizationHeader("viewer"))
                .whenPost("content", "/com/breece/app/content/content-details.json")
                .expectResult(ContentId.class).expectEvents(CreateContent.class);
    }

    @Test
    void getContents() {
        testFixture
                .withHeader("Authorization", createAuthorizationHeader("viewer"))
                .givenPost("content", "/com/breece/app/content/content-details.json")
                .whenGet("content")
                .expectResult(hasSize(1));
    }

    String createAuthorizationHeader(String user) {
        return testFixture.getFluxzero().apply(
                fc -> AuthenticationUtils.createAuthorizationHeader(new UserId(user)));
    }
}
