package com.breece.trackrejoice;

import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ContentState;
import com.breece.trackrejoice.content.model.ContentStatus;
import com.breece.trackrejoice.content.query.GetClassifiedAdState;
import com.breece.trackrejoice.orders.api.command.ValidateOrder;
import com.breece.trackrejoice.orders.api.model.OrderId;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

class ContentStateTest {
    TestFixture testFixture = TestFixture.create(ContentState.class).givenCommands("service/create-service.json", "content/create-content.json");

    @Test
    void createContent() {
        testFixture
                .whenQuery(new GetClassifiedAdState(new ContentId("1")))
                .expectResult(ContentState.class)
                .expectResult(state -> ContentStatus.DRAFT == state.status());
    }

    @Test
    void onPlaceOrder() {
        testFixture.givenCommands("orders/place-order.json")
                .whenQuery(new GetClassifiedAdState(new ContentId("1")))
                .expectResult(ContentState.class)
                .expectResult(state -> ContentStatus.PENDING_PAYMENT == state.status());
    }

    @Test
    void onValidateOrder() {
        testFixture.givenCommands("orders/place-order.json")
                .givenEvents(new ValidateOrder(new OrderId("1"), new ContentId("1"),"some reference"))
                .whenQuery(new GetClassifiedAdState(new ContentId("1")))
                .expectNoErrors()
                .expectResult(ContentState.class)
                .expectResult(state -> ContentStatus.ENABLED == state.status());
    }

}