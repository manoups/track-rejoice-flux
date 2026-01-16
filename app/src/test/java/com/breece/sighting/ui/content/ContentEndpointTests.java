package com.breece.sighting.ui.content;

import com.breece.common.model.ContentId;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.CreateContent;
import com.breece.sighting.ui.ContentEndpoint;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;

public class ContentEndpointTests {
    final TestFixture testFixture = TestFixture.create(new ContentEndpoint(), ContentState.class);

    @Test
    void createContent() {
        testFixture.whenPost("content", "/com/breece/sighting/ui/content/content-details.json")
                .expectResult(ContentId.class).expectEvents(CreateContent.class);
    }

    @Test
    void getContents() {
        testFixture.givenPost("content", "/com/breece/sighting/ui/content/content-details.json")
                .whenGet("content")
                .expectResult(hasSize(1));
    }
}
