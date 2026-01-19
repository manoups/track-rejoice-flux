package com.breece.app;

import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.CreateContent;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;

public class ContentEndpointTests {
    final TestFixture testFixture = TestFixture.create(new ContentEndpoint(), ContentState.class);

    @Test
    void createContent() {
        testFixture.whenPost("content", "/com/breece/app/content/content-details.json")
                .expectResult(ContentId.class).expectEvents(CreateContent.class);
    }

    @Test
    void getContents() {
        testFixture.givenPost("content", "/com/breece/app/content/content-details.json")
                .whenGet("content")
                .expectResult(hasSize(1));
    }
}
