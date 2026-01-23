package com.breece.app.web.support;

import io.fluxzero.proxy.ProxyRequestHandler;
import io.fluxzero.proxy.ProxyServer;
import io.fluxzero.sdk.configuration.client.Client;
import io.fluxzero.sdk.configuration.client.WebSocketClient;
import io.fluxzero.sdk.tracking.handling.authentication.UserProvider;
import io.fluxzero.testserver.TestServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@TestConfiguration
public class UiUpdaterTestConfig {
    @Bean
    public UserProvider userProvider() {
        return new FixedUserProvider();
    }

    @Bean
    public Client fluxzeroClient() {
        TestServer.start(UiUpdaterTestSupport.TEST_SERVER_PORT);
        TestPorts.waitForPort(UiUpdaterTestSupport.HOST, UiUpdaterTestSupport.TEST_SERVER_PORT, Duration.ofSeconds(5));
        return WebSocketClient.newInstance(WebSocketClient.ClientConfig.builder()
                .runtimeBaseUrl("ws://" + UiUpdaterTestSupport.HOST + ":" + UiUpdaterTestSupport.TEST_SERVER_PORT)
                .name("track-rejoice-test")
                .projectId("test")
                .build());
    }

    @Bean(destroyMethod = "cancel")
    public ProxyServer proxyServer(Client fluxzeroClient) {
        ProxyServer proxyServer = ProxyServer.start(UiUpdaterTestSupport.PROXY_PORT, new ProxyRequestHandler(fluxzeroClient));
        TestPorts.waitForPort(UiUpdaterTestSupport.HOST, UiUpdaterTestSupport.PROXY_PORT, Duration.ofSeconds(5));
        return proxyServer;
    }
}
