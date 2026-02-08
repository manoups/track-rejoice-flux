package com.breece.app;

import com.breece.app.util.TestUtil;
import com.breece.app.web.ContentEndpoint;
import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.CreateContent;
import com.breece.content.command.api.DeleteContent;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
public class ContentEndpointTests extends TestUtil {
    protected final TestFixture testFixture = TestFixture.create(new ContentEndpoint(), ContentState.class)
            .givenCommands("user/create-user.json");

    @Test
    void createContent() {
        testFixture
                .withHeader("Authorization", createAuthorizationHeader("viewer"))
                .whenPost("api/content", "/com/breece/app/content/content-details.json")
                .expectResult(ContentId.class).expectEvents(CreateContent.class);
    }

    @Test
    void getContents() {
        testFixture
                .withHeader("Authorization", createAuthorizationHeader("viewer"))
                .givenPost("api/content", "/com/breece/app/content/content-details.json")
                .whenGet("api/content")
                .expectResult(hasSize(1));
    }

    @Test
    void deleteContent() {
        testFixture
                .withHeader("Authorization", createAuthorizationHeader("viewer"))
                .givenCommandsByUser("viewer", "/com/breece/app/content/content-details-command.json")
                .whenDelete("api/content/1")
                .expectResult(ContentId.class).expectOnlyEvents(DeleteContent.class);
    }


    @Override
    protected TestFixture testFixture() {
        return testFixture;
    }
}
