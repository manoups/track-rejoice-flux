package com.breece.sighting.ui;

import com.breece.common.model.ContentId;
import com.breece.common.model.ContentStatus;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.GetContentState;
import com.breece.order.api.model.OrderId;
import com.breece.order.api.command.ValidateOrder;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

class ContentStateTest {
    TestFixture testFixture = TestFixture.create(ContentState.class).givenCommands("service/create-service.json", "content/create-content.json");

    @Test
    void createContent() {
        testFixture
                .whenQuery(new GetContentState(new ContentId("1")))
                .expectResult(ContentState.class)
                .expectResult(state -> ContentStatus.OFFLINE == state.status());
    }

    @Test
    void onPlaceOrder() {
        testFixture.givenCommands("orders/place-order.json")
                .whenQuery(new GetContentState(new ContentId("1")))
                .expectResult(ContentState.class)
                .expectResult(state -> ContentStatus.OFFLINE == state.status());
    }

    @Test
    void onValidateOrder() {
        testFixture.givenCommands("orders/place-order.json")
                .givenEvents(new ValidateOrder(new OrderId("1"), new ContentId("1"),"some reference"))
                .whenQuery(new GetContentState(new ContentId("1")))
                .expectNoErrors()
                .expectResult(ContentState.class)
                .expectResult(state -> ContentStatus.OFFLINE == state.status());
    }

}