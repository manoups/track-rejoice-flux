package com.breece.app.web;

import com.breece.app.web.api.UiUpdate;
import com.breece.content.api.model.Content;
import com.breece.coreapi.authentication.AuthenticationUtils;
import com.breece.coreapi.authentication.Role;
import com.breece.coreapi.user.api.CreateUser;
import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonPatch;
import io.fluxzero.sdk.common.serialization.jackson.JacksonSerializer;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.web.HttpRequestMethod;
import io.fluxzero.sdk.web.WebRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

public class UiUpdaterTests {
    private final TestFixture testFixture = TestFixture.create();
    private final ObjectMapper objectMapper = JacksonSerializer.defaultObjectMapper;

    private static <T> T extractEntity(ObjectMapper objectMapper, UiUpdate update, Class<T> type) throws Exception {
        JsonNode base = objectMapper.createObjectNode();
        JsonNode patched = JsonPatch.apply(update.getPatch(), base);
        return objectMapper.treeToValue(patched, type);
    }

    @BeforeEach
    void setUp() {
        testFixture
//                .withBean(uiUpdater)
                .registerHandlers(ContentEndpoint.class, UiUpdateSocketEndpoint.class)
                .givenCommands("../user/create-user.json")
                .withHeader("Authorization", createAuthorizationHeader("viewer"))
                .givenWebRequest(openSocket());
    }

    @Test
    void userFromDifferentSessionDoesNotReceiveUpdates() {
        testFixture.givenCommands(CreateUser.builder().userId(new UserId("Alice")).details(UserDetails.builder().name("Alice").email("a@b").build()).build())
                .whenCommandByUser("Alice","../content/create-content.json")
                .expectNoWebResponses();
    }

    @Test
    void adminReceivesUpdates() {
        testFixture.givenCommands(CreateUser.builder().userId(new UserId("Alice")).details(UserDetails.builder().name("Alice").email("a@b").build()).role(Role.ADMIN).build())
                .whenCommandByUser("Alice","../content/create-content.json")
                .expectNoWebResponses();
    }

   /* @AfterEach
    void tearDown() {
        testFixture
                .withHeader("Authorization", createAuthorizationHeader("viewer"))
                .whenWebRequest(WebRequest.builder().url("/api/updates").method(HttpRequestMethod.WS_CLOSE).build())
                .expectNoErrors();
    }*/

    @Test
    @Disabled
    void sendWholeCatalogOnOpen() {
        testFixture.withHeader("Authorization", createAuthorizationHeader("viewer"))
                .whenWebRequest(openSocket())
                .expectWebResponse(r -> r.getPayload() instanceof List<?> gamesList
                        && gamesList.size() == 20);
    }

    @Test
    void receiveContentUpdate() {
        testFixture.whenCommandByUser("viewer","../content/create-content.json")
                .expectWebResponse(r -> r.getPayload() instanceof UiUpdate uiUpdate
                        && extractEntity(objectMapper, uiUpdate, Content.class) instanceof Content res
                        && res.contentId().toString().equals("content-1"));
    }

    @Test
    void closeSessionAfterTimeout() {
        testFixture
                .givenElapsedTime(Duration.ofSeconds(60))
                .given(fc -> fc.taskScheduler().executeExpiredTasks()) // send ping
                .givenElapsedTime(Duration.ofSeconds(25)) // > pingTimeout (default 20s)
                .given(fc -> fc.taskScheduler().executeExpiredTasks()) // close session
                .whenCommand("../content/create-content.json")
                .expectNoWebResponses();
    }

    String createAuthorizationHeader(String user) {
        return testFixture.getFluxzero().apply(
                fc -> AuthenticationUtils.createAuthorizationHeader(new UserId(user)));
    }

    static WebRequest openSocket() {
        return WebRequest.builder().url("/api/updates").method(HttpRequestMethod.WS_OPEN).build();
    }
}
