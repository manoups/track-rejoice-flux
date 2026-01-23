package com.breece.app.web;

import com.breece.app.App;
import com.breece.app.web.support.TestSocketListener;
import com.breece.app.web.support.UiUpdateTestHelper;
import com.breece.app.web.support.UiUpdaterTestSupport;
import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.GenderEnum;
import com.breece.content.api.model.Pet;
import com.breece.content.command.api.CreateContent;
import com.breece.coreapi.authentication.Role;
import com.breece.coreapi.user.api.CreateUser;
import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserDetails;
import com.breece.sighting.api.model.SightingDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.common.serialization.jackson.JacksonSerializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@SpringBootTest(classes = App.class)
@Import(com.breece.app.web.support.UiUpdaterTestConfig.class)
class UiUpdaterTest {
    private final ObjectMapper objectMapper = JacksonSerializer.defaultObjectMapper;

    @Test
    void sendsUiUpdatesOverWebsocket() throws Exception {
        UserId userId = new UserId("viewer");
        Fluxzero.sendCommandAndWait(new CreateUser(
                userId,
                UserDetails.builder()
                        .name("Test")
                        .email("test@example.com")
                        .build(),
                Role.MANAGER));
//        testFixture.whenCommand("../user/create-user.json");

        CompletableFuture<Void> openFuture = new CompletableFuture<>();
        CompletableFuture<String> messageFuture = new CompletableFuture<>();
        WebSocket.Listener listener = new TestSocketListener(openFuture, messageFuture);
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        WebSocket webSocket = httpClient
                .newWebSocketBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .buildAsync(URI.create("ws://" + UiUpdaterTestSupport.HOST + ":" + UiUpdaterTestSupport.PROXY_PORT
                        + "/api/updates"), listener)
                .orTimeout(5, TimeUnit.SECONDS)
                .join();

        openFuture.orTimeout(5, TimeUnit.SECONDS).join();
        Thread.sleep(200);
        Fluxzero.sendCommandAndWait(new CreateContent(new ContentId("1"), new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), new Pet("name", "breed", GenderEnum.FEMALE)));

        String payload = messageFuture.orTimeout(5, TimeUnit.SECONDS).join();

        Assertions.assertThat(payload)
                .contains("\"type\":\"Content\"")
                .contains("\"id\":\"content-1\"")
                .contains("op");
        Content content = UiUpdateTestHelper.extractContent(objectMapper, payload);
        Assertions.assertThat(content.contentId().getFunctionalId()).isEqualTo("1");
        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "done").join();
    }

}
