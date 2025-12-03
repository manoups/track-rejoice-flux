package com.breece.trackrejoice.classifiedsad.model;

import com.breece.trackrejoice.classifiedsad.query.GetClassifiedAdState;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

class ClassifiedsAdStateTest {
    TestFixture testFixture = TestFixture.create(ClassifiedsAdState.class);

    @Test
    void createClassifiedsAd() {
        testFixture.givenCommands("create-classifieds-ad.json")
                .whenQuery(new GetClassifiedAdState(new ClassifiedsAdId("1")))
                .expectResult(ClassifiedsAdState.class)
                .expectResult(state -> ClassifiedsAdStatus.DRAFT == state.status());
    }

}