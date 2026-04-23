package com.breece.app;

import com.breece.app.web.ServiceEndpoint;
import com.breece.service.api.GetServices;
import com.breece.service.api.model.Service;
import com.breece.service.api.model.ServiceDetails;
import com.breece.service.api.model.ServiceId;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;

public class ServiceEndpointTests {

    static class BasicOnlineServiceHandler {
        @HandleQuery
        public List<Service> handle(GetServices request) {
            return List.of(new Service(
                    new ServiceId("1"),
                    new ServiceDetails("Basic Service", "Basic online service", BigDecimal.valueOf(100)),
                    true,
                    true
            ));
        }
    }

    static class EmptyServiceHandler {
        @HandleQuery
        public List<Service> handle(GetServices request) {
            return Collections.emptyList();
        }
    }

    @Nested
    class WhenBasicOnlineServiceExists {
        final TestFixture testFixture = TestFixture.create(new ServiceEndpoint(), new BasicOnlineServiceHandler());

        @Test
        void getBasicService() {
            testFixture
                    .whenGet("api/services/basic")
                    .expectResult(Service.class);
        }
    }

    @Nested
    class WhenNoBasicServiceExists {
        final TestFixture testFixture = TestFixture.create(new ServiceEndpoint(), new EmptyServiceHandler());

        @Test
        void getBasicServiceReturnsNull() {
            testFixture
                    .whenGet("api/services/basic")
                    .expectResult(nullValue());
        }
    }
}
