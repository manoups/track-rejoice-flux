package com.breece.app;

import com.breece.app.web.ServiceEndpoint;
import com.breece.service.api.model.Service;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class ServiceEndpointTests {
    final TestFixture testFixture = TestFixture.create(new ServiceEndpoint());

    @Test
    void getBasicService() {
        testFixture
                .givenCommands("service/create-basic-online-service.json")
                .whenGet("api/services/basic")
                .expectResult(Service.class);
    }

    @Test
    void getBasicServiceWhenNoneExists() {
        testFixture
                .whenGet("api/services/basic")
                .expectResult(nullValue());
    }
}
