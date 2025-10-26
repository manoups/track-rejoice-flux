package com.breece.trackrejoice.classifiedsad.model;

import com.breece.trackrejoice.classifiedsad.ClassifiedsAdEndpoint;
import com.breece.trackrejoice.classifiedsad.ClassifiedsAdScheduler;
import com.breece.trackrejoice.classifiedsad.ExecutePayment;
import com.breece.trackrejoice.classifiedsad.command.CreateClassifiedsAd;
import com.breece.trackrejoice.classifiedsad.command.DeleteClassifiedsAd;
import com.breece.trackrejoice.classifiedsad.query.GetClassifiedAds;
import com.breece.trackrejoice.orders.api.PaymentProcess;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;

class ClassifiedsAdTest {
    final TestFixture testFixture = TestFixture.create();

    @Test
    void createClassifiedsAd() {
        testFixture.whenCommand("/classifiedsad/create-classifieds-ad.json")
                .expectEvents("/classifiedsad/create-classifieds-ad.json");
    }

    @Test
    void createClassifiedsAdKeys() {
        testFixture.whenCommand("/classifiedsad/create-classifieds-ad-keys.json")
                .expectEvents("/classifiedsad/create-classifieds-ad-keys.json");
    }

    @Test
    void updateClassifiedsAd() {
        testFixture.givenCommands("/classifiedsad/create-classifieds-ad.json")
                .whenCommand("/classifiedsad/update-classifieds-ad.json")
                .expectEvents("/classifiedsad/update-classifieds-ad.json");
    }



    @Test
    void deleteClassifiedsAd() {
        testFixture.givenCommands("/classifiedsad/create-classifieds-ad.json")
                .whenCommand("/classifiedsad/delete-classifieds-ad.json")
                .expectEvents("/classifiedsad/delete-classifieds-ad.json");
    }

    @Nested
    class ClassifiedsAdQueryTests {
        @Test
        void searchForClassifiedsAd() {
            testFixture.givenCommands("/classifiedsad/create-classifieds-ad.json")
                    .whenQuery(new GetClassifiedAds())
                    .expectResult(hasSize(1));
        }
    }

    @Nested
    class ClassifiedsAdIntegrationTests {
        @Test
        void deleteClassifiedsAd() {
            testFixture.givenCommands("/classifiedsad/create-classifieds-ad.json")
                .whenQuery(new GetClassifiedAds())
                .expectResult(hasSize(1))
                .andThen()
                    .whenCommand("/classifiedsad/delete-classifieds-ad.json")
                    .expectEvents("/classifiedsad/delete-classifieds-ad.json")
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
            testFixture.whenPost("/classifieds-ads", "/classifiedsad/classifieds-ad-details.json")
                    .expectResult(ClassifiedsAdId.class).expectEvents(CreateClassifiedsAd.class);
        }

        @Test
        void getClassifiedsAds() {
            testFixture.givenPost("/classifieds-ads", "/classifiedsad/classifieds-ad-details.json")
                    .whenGet("/classifieds-ads")
                    .expectResult(hasSize(1));
        }
    }

    @Nested
    class ClassifiedsAdSchedulerTests {
        @BeforeEach
        void setUp() {
            testFixture.registerHandlers(new ClassifiedsAdScheduler()).givenCommands("/classifiedsad/create-classifieds-ad.json");
        }

        @Test
        void createClassifiedsAdDetails() {
            testFixture
                    .whenEvent("/classifiedsad/payment-confirmed.json")
                    .andThen()
                    .whenTimeElapses(Duration.ofDays(90))
                    .expectEvents(DeleteClassifiedsAd.class);
        }
    }

    @Nested
    class ClassifiedsAdStateTests {
        @BeforeEach
        void setUp() {
            testFixture.registerHandlers(PaymentProcess.class).givenCommands("/classifiedsad/create-classifieds-ad.json");
        }

        @Disabled
        @Timeout(15)
        @Test
        void createClassifiedsAdDetails() {
            testFixture.whenEvent("/classifiedsad/payment-initiated.json")
                    .expectEvents(ExecutePayment.class);
        }
    }
}