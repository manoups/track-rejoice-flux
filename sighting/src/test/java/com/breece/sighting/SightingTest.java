package com.breece.sighting;

import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.common.SightingEnum;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.query.api.GetSightingStats;
import com.breece.sighting.query.api.GetSightings;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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

    @Test
    void searchPaginatedContent() {
        final int SIZE = 25;
        CreateSighting[] contents = new CreateSighting[SIZE];
        for (int i = 0; i < 15; ++i) {
            SightingId sightingId = new SightingId();
            contents[i] = new CreateSighting(sightingId, new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), i % 2 == 0, SightingEnum.DOG);
        }
        for (int i = 15; i < SIZE; ++i) {
            SightingId contentId = new SightingId();
            contents[i] = new CreateSighting(contentId, new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), i % 2 == 0, SightingEnum.KEYS);
        }

        testFixture.givenCommands(contents)
                .whenQuery(new GetSightings())
                .expectResult(hasSize(10))
                .andThen()
                .whenQuery(new GetSightings(1, 10))
                .expectResult(hasSize(10))
                .andThen()
                .whenQuery(new GetSightings(2, 10))
                .expectResult(hasSize(5))
                .andThen()
                .whenQuery(new GetSightings(0, 30))
                .expectResult(hasSize(25))
                .andThen()
                .whenQuery(new GetSightingStats())
                .expectResult(facetStats -> facetStats.size() == 2 &&
                        facetStats.stream().filter(it -> it.getValue().equals("DOG")).findFirst().orElseThrow(() -> new AssertionError("No pet facet found")).getCount() == 15 &&
                        facetStats.stream().filter(it -> it.getValue().equals("KEYS")).findFirst().orElseThrow(() -> new AssertionError("No keys facet found")).getCount() == 10);
    }
}
