package com.breece.sighting;

import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.common.SightingEnum;
import com.breece.coreapi.facets.GetFacetStatsResult;
import com.breece.coreapi.facets.GetFacets;
import com.breece.coreapi.facets.Pagination;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.query.api.GetSightings;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

public class SightingTest {
    final TestFixture testFixture = TestFixture.create();
    static CreateSighting[] contents;

    @BeforeAll
    static void setUp() {
        final int SIZE = 25;
        contents = new CreateSighting[SIZE];
        for (int i = 0; i < 15; ++i) {
            SightingId sightingId = new SightingId();
            contents[i] = new CreateSighting(sightingId, new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), i % 2 == 0, i % 2 == 0 ? SightingEnum.DOG : SightingEnum.CAT);
        }
        for (int i = 15; i < SIZE; ++i) {
            SightingId contentId = new SightingId();
            contents[i] = new CreateSighting(contentId, new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), i % 2 == 0, getRandomSightingEnum(i));
        }
    }

    static SightingEnum getRandomSightingEnum(int i) {
        return switch (i % 3) {
            case 0 -> SightingEnum.JEWELERY;
            case 1 -> SightingEnum.KEYS;
            default -> SightingEnum.CARD;
        };
    }

    @Test
    void createSighting() {
        testFixture.whenCommand("create-sighting.json")
                .expectEvents("create-sighting.json");
    }

    @Test
    void givenSighting_whenGetSightings_thenOneResults() {
        testFixture
                .givenCommands("create-sighting.json")
                .whenQuery(new GetSightings(Collections.emptyList(), null, new Pagination(0, 10)))
                .expectResult(hasSize(1));
    }

    @Test
    void searchPaginatedContent() {
        testFixture.givenCommands(contents)
                .whenQuery(new GetSightings(Collections.emptyList(), null, new Pagination(0, 10)))
                .expectResult(hasSize(10))
                .andThen()
                .whenQuery(new GetSightings(Collections.emptyList(), null, new Pagination(1, 10)))
                .expectResult(hasSize(10))
                .andThen()
                .whenQuery(new GetSightings(Collections.emptyList(), null, new Pagination(2, 10)))
                .expectResult(hasSize(5))
                .andThen()
                .whenQuery(new GetSightings(Collections.emptyList(), null, new Pagination(0, 30)))
                .expectResult(hasSize(25))
                .andThen()
                .whenQuery(new GetFacets(new GetSightings(Collections.emptyList(), null, new Pagination(0, 10))))
                .expectResult(Objects::nonNull)
                .mapResult(GetFacetStatsResult::getStats)
                .expectResult(facetStats -> facetStats.size() == SightingEnum.values().length &&
                        facetStats.stream().filter(it -> it.value().equals(SightingEnum.DOG.name())).findFirst().orElseThrow(() -> new AssertionError("No pet facet found")).count() == 8 &&
                        facetStats.stream().filter(it -> it.value().equals(SightingEnum.CAT.name())).findFirst().orElseThrow(() -> new AssertionError("No pet facet found")).count() == 7 &&
                        facetStats.stream().filter(it -> it.value().equals(SightingEnum.JEWELERY.name())).findFirst().orElseThrow(() -> new AssertionError("No pet facet found")).count() == 4 &&
                        facetStats.stream().filter(it -> it.value().equals(SightingEnum.CARD.name())).findFirst().orElseThrow(() -> new AssertionError("No pet facet found")).count() == 3 &&
                        facetStats.stream().filter(it -> it.value().equals(SightingEnum.KEYS.name())).findFirst().orElseThrow(() -> new AssertionError("No keys facet found")).count() == 3);
    }
}
