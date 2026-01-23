package com.breece.app.web;

import com.breece.app.App;
import com.breece.app.web.api.UiUpdate;
import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.GenderEnum;
import com.breece.content.api.model.Pet;
import com.breece.content.command.api.CreateContent;
import com.breece.coreapi.authentication.Role;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.user.api.CreateUser;
import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserDetails;
import com.breece.sighting.api.model.SightingDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fluxzero.common.MessageType;
import io.fluxzero.proxy.ProxyRequestHandler;
import io.fluxzero.proxy.ProxyServer;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.common.HasMessage;
import io.fluxzero.sdk.common.serialization.DeserializingMessage;
import io.fluxzero.sdk.common.serialization.jackson.JacksonSerializer;
import io.fluxzero.sdk.configuration.client.Client;
import io.fluxzero.sdk.configuration.client.WebSocketClient;
import io.fluxzero.sdk.tracking.handling.authentication.AbstractUserProvider;
import io.fluxzero.sdk.tracking.handling.authentication.User;
import io.fluxzero.sdk.tracking.handling.authentication.UserProvider;
import io.fluxzero.testserver.TestServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;


@SpringBootTest(classes = App.class)
@Import(UiUpdaterTest.TestConfig.class)
class UiUpdaterTest {
    private static final int TEST_SERVER_PORT = 8090;
    private static final int PROXY_PORT = 8091;
    private static final String HOST = "127.0.0.1";
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
        WebSocket.Listener listener = new TextListener(openFuture, messageFuture);
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        WebSocket webSocket = httpClient
                .newWebSocketBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .buildAsync(URI.create("ws://" + HOST + ":" + PROXY_PORT + "/api/updates"), listener)
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
        UiUpdate update = objectMapper.readValue(payload, UiUpdate.class);
        Content content = extractContent(update);
        Assertions.assertThat(content.contentId().getFunctionalId()).isEqualTo("1");
        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "done").join();
    }

    private static class TextListener implements WebSocket.Listener {
        private final CompletableFuture<Void> openFuture;
        private final CompletableFuture<String> messageFuture;
        private final StringBuilder buffer = new StringBuilder();

        private TextListener(CompletableFuture<Void> openFuture, CompletableFuture<String> messageFuture) {
            this.openFuture = openFuture;
            this.messageFuture = messageFuture;
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            openFuture.complete(null);
            webSocket.request(1);
        }


        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            if (!openFuture.isDone()) {
                openFuture.completeExceptionally(new IllegalStateException("Closed before open: " + reason));
            }
            if (!messageFuture.isDone()) {
                messageFuture.completeExceptionally(new IllegalStateException("Closed: " + reason));
            }
            return null;
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            buffer.append(data);
            if (last && !messageFuture.isDone()) {
                messageFuture.complete(buffer.toString());
            }
            webSocket.request(1);
            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            if (!openFuture.isDone()) {
                openFuture.completeExceptionally(error);
            }
            messageFuture.completeExceptionally(error);
        }
    }


    private static void waitForPort(String host, int port, Duration timeout) {
        long deadline = System.nanoTime() + timeout.toNanos();
        while (System.nanoTime() < deadline) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 200);
                return;
            } catch (Exception ignored) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Interrupted while waiting for " + host + ":" + port);
                }
            }
        }
        throw new IllegalStateException("Timed out waiting for " + host + ":" + port);
    }

    private Content extractContent(UiUpdate update) throws Exception {
        JsonNode patch = update.getPatch();
        JsonNode value = patch.get(0).get("value");
        return objectMapper.treeToValue(value, Content.class);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserProvider userProvider() {
            return new FixedUserProvider();
        }

        @Bean
        Client fluxzeroClient() {
            TestServer.start(TEST_SERVER_PORT);
            waitForPort(HOST, TEST_SERVER_PORT, Duration.ofSeconds(5));
            return WebSocketClient.newInstance(WebSocketClient.ClientConfig.builder()
                    .runtimeBaseUrl("ws://" + HOST + ":" + TEST_SERVER_PORT)
                    .name("track-rejoice-test")
                    .projectId("test")
                    .build());
        }

        @Bean(destroyMethod = "cancel")
        ProxyServer proxyServer(Client fluxzeroClient) {
            ProxyServer proxyServer = ProxyServer.start(PROXY_PORT, new ProxyRequestHandler(fluxzeroClient));
            waitForPort(HOST, PROXY_PORT, Duration.ofSeconds(5));
            return proxyServer;
        }
    }

    private static class FixedUserProvider extends AbstractUserProvider {
        private final Sender webUser = Sender.builder()
                .userId(new UserId("viewer"))
                .userRole(Role.MANAGER)
                .build();
        private final Sender systemUser = Sender.builder()
                .userId(new UserId("admin"))
                .userRole(Role.ADMIN)
                .build();

        private FixedUserProvider() {
            super(Sender.class);
        }

        @Override
        public User fromMessage(HasMessage message) {
            if (message instanceof DeserializingMessage dm && dm.getMessageType() == MessageType.WEBREQUEST) {
                return webUser;
            }
            return super.fromMessage(message);
        }

        @Override
        public User getUserById(Object userId) {
            if (userId instanceof UserId id) {
                return resolveUser(id.getFunctionalId());
            }
            if (userId instanceof String id) {
                return resolveUser(id);
            }
            return null;
        }

        @Override
        public User getSystemUser() {
            return systemUser;
        }

        private User resolveUser(String id) {
            if ("user".equals(id)) {
                return webUser;
            }
            if ("admin".equals(id)) {
                return systemUser;
            }
            return null;
        }
    }
}
