package com.breece.sighting;

import com.breece.sighting.query.api.GetSightings;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;

public class SightingTest {
    final TestFixture testFixture = TestFixture.create();

    @Test
    void createSighting() {
        testFixture.whenCommand("create-sighting.json")
                .expectEvents("create-sighting.json");
    }

    @Test
    void givenSighting_whenGetSightings_thenOneResults() {
        testFixture
                .givenCommands("create-sighting.json")
                .whenQuery(new GetSightings())
                .expectResult(hasSize(1));
    }
}
