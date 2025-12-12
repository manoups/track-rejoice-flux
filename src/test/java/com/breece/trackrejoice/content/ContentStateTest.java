package com.breece.trackrejoice.content;

import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ContentState;
import com.breece.trackrejoice.content.model.ContentStatus;
import com.breece.trackrejoice.content.query.GetClassifiedAdState;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

class ContentStateTest {
    TestFixture testFixture = TestFixture.create(ContentState.class);

    @Test
    void createContent() {
        testFixture.givenCommands("create-content.json")
                .whenQuery(new GetClassifiedAdState(new ContentId("1")))
                .expectResult(ContentState.class)
                .expectResult(state -> ContentStatus.DRAFT == state.status());
    }

}