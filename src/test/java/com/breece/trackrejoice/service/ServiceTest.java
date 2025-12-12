package com.breece.trackrejoice.service;

import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

class ServiceTest {
    TestFixture testFixture = TestFixture.create();

    @Test
    void createService() {
        testFixture.whenCommand("create-service.json")
                .expectEvents("create-service.json");
    }

    @Test
    void updateService() {
        testFixture.givenCommands("create-service.json").whenCommand("update-service.json")
                .expectEvents("update-service.json");
    }
}