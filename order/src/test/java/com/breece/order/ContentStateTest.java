package com.breece.order;

import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.ContentStatus;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.GetContentState;
import com.breece.order.api.order.model.OrderId;
import com.breece.order.api.command.CreateOrderRemote;
import com.breece.util.MockQueryHandler;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import java.util.List;

class ContentStateTest {
    TestFixture testFixture = TestFixture.create(ContentState.class, new MockQueryHandler()).givenCommands("../content/create-content.json");

    @Test
    void createContent() {
        testFixture
                .whenQuery(new GetContentState(new ContentId("1")))
                .expectResult(ContentState.class)
                .expectResult(state -> ContentStatus.OFFLINE == state.status());
    }

    @Test
    void onPlaceOrder() {
        testFixture.givenCommands("place-order.json")
                .whenQuery(new GetContentState(new ContentId("1")))
                .expectResult(ContentState.class)
                .expectResult(state -> ContentStatus.OFFLINE == state.status());
    }

    @Test
    void onValidateOrder() {
        testFixture.givenCommands("place-order.json")
                .givenEvents(new CreateOrderRemote(new OrderId("1"),"some reference", List.of()))
                .whenQuery(new GetContentState(new ContentId("1")))
                .expectNoErrors()
                .expectResult(ContentState.class)
                .expectResult(state -> ContentStatus.OFFLINE == state.status());
    }

}