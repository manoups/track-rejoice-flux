package com.breece.trackrejoice.classifiedsad.model;

import com.breece.trackrejoice.classifiedsad.ClassifiedsAdEndpoint;
import com.breece.trackrejoice.classifiedsad.ClassifiedsAdScheduler;
import com.breece.trackrejoice.classifiedsad.ExecutePayment;
import com.breece.trackrejoice.classifiedsad.command.CreateClassifiedsAd;
import com.breece.trackrejoice.classifiedsad.command.TakeClassifiedsAdOffline;
import com.breece.trackrejoice.classifiedsad.query.GetClassifiedAds;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;

class ClassifiedsAdTest {
    final TestFixture testFixture = TestFixture.create();

    @Test
    void createClassifiedsAd() {
        testFixture.whenCommand("create-classifieds-ad.json")
                .expectEvents("create-classifieds-ad.json");
    }

    @Test
    void createClassifiedsAdKeys() {
        testFixture.whenCommand("create-classifieds-ad-keys.json")
                .expectEvents("create-classifieds-ad-keys.json");
    }

    @Test
    void updateClassifiedsAd() {
        testFixture.givenCommands("create-classifieds-ad.json")
                .whenCommand("update-classifieds-ad.json")
                .expectEvents("update-classifieds-ad.json");
    }



    @Test
    void deleteClassifiedsAd() {
        testFixture.givenCommands("create-classifieds-ad.json")
                .whenCommand("delete-classifieds-ad.json")
                .expectEvents("delete-classifieds-ad.json");
    }

    @Nested
    class ClassifiedsAdQueryTests {
        @Test
        void searchForClassifiedsAd() {
            testFixture.givenCommands("create-classifieds-ad.json")
                    .whenQuery(new GetClassifiedAds())
                    .expectResult(hasSize(1));
        }
    }

    @Nested
    class ClassifiedsAdIntegrationTests {
        @Test
        void deleteClassifiedsAd() {
            testFixture.givenCommands("create-classifieds-ad.json")
                .whenQuery(new GetClassifiedAds())
                .expectResult(hasSize(1))
                .andThen()
                    .whenCommand("delete-classifieds-ad.json")
                    .expectEvents("delete-classifieds-ad.json")
                .andThen()
                .whenQuery(new GetClassifiedAds())
                .expectResult(List::isEmpty);
        }
    }

    @Nested
    class ClassifiedsAdEndpointTests {
        @BeforeEach
        void setUp() {
            testFixture.registerHandlers(new ClassifiedsAdEndpoint());
        }

        @Test
        void createClassifiedsAd() {
            testFixture.whenPost("classifieds-ads", "/com/breece/trackrejoice/classifiedsad/model/classifieds-ad-details.json")
                    .expectResult(ClassifiedsAdId.class).expectEvents(CreateClassifiedsAd.class);
        }

        @Test
        void getClassifiedsAds() {
            testFixture.givenPost("classifieds-ads", "/com/breece/trackrejoice/classifiedsad/model/classifieds-ad-details.json")
                    .whenGet("classifieds-ads")
                    .expectResult(hasSize(1));
        }
    }

    @Nested
    class ClassifiedsAdSchedulerTests {
        @BeforeEach
        void setUp() {
            testFixture.registerHandlers(new ClassifiedsAdScheduler()).givenCommands("create-classifieds-ad.json");
        }

        @Test
        void createClassifiedsAdDetails() {
            testFixture.givenCommands("/com/breece/trackrejoice/orders/api/model/place-order.json")
                    .whenEvent("payment-accepted.json")
                    .andThen()
                    .whenTimeElapses(Duration.ofDays(90))
                    .expectEvents(TakeClassifiedsAdOffline.class);
        }
    }

    @Nested
    class ClassifiedsAdStateTests {
        @BeforeEach
        void setUp() {
            testFixture.registerHandlers().givenCommands("create-classifieds-ad.json");
        }

        @Disabled
        @Timeout(15)
        @Test
        void createClassifiedsAdDetails() {
            testFixture.whenEvent("payment-initiated.json")
                    .expectEvents(ExecutePayment.class);
        }
    }
}