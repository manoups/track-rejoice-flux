package content;

import com.breece.content.ContentErrors;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.GenderEnum;
import com.breece.content.api.model.Keys;
import com.breece.content.api.model.Pet;
import com.breece.content.command.api.ContentScheduler;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.CreateContent;
import com.breece.content.command.api.TakeContentOffline;
import com.breece.content.query.api.ContentDocument;
import com.breece.content.query.api.GetContents;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.common.SightingEnum;
import com.breece.coreapi.facets.GetFacets;
import com.breece.coreapi.facets.Pagination;
import io.fluxzero.common.api.search.GetFacetStatsResult;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import util.TestUtilities;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

class ContentTest extends TestUtilities {
    final TestFixture testFixture = TestFixture.create(ContentState.class).givenCommands(createUserFromProfile(viewer), createUserFromProfile(user2), createUserFromProfile(Alice));

    @Test
    void createContent() {
        testFixture.whenCommand("create-content.json")
                .expectEvents("create-content.json");
    }

    @Test
    void createContentKeys() {
        testFixture.whenCommand("create-content-keys.json")
                .expectEvents("create-content-keys.json");
    }

    @Test
    void updateContent() {
        testFixture.givenCommands("create-content.json")
                .whenCommand("update-content.json")
                .expectEvents("update-content.json");
    }

    @Test
    void deleteContent() {
        testFixture.givenCommands("create-content.json")
                .whenCommand("delete-content.json")
                .expectEvents("delete-content.json");
    }

    @Test
    void deleteContentNonOwner() {
        testFixture.givenCommandsByUser("viewer", "create-content.json")
                .whenCommandByUser("user2", "delete-content.json")
                .expectExceptionalResult(ContentErrors.unauthorized)
                .expectError((e) -> e.getMessage().equals(ContentErrors.unauthorized.getMessage()));
    }

    @Nested
    class ContentQueryTests {
        @Test
        void searchForContent() {
            testFixture.givenCommands("create-content.json")
                    .whenQuery(new GetContents(Collections.emptyList(), null, new Pagination(0, 10)))
                    .expectResult(hasSize(1));
        }

        @Test
        void searchPaginatedContent() {
            final int SIZE = 25;
            CreateContent[] contents = new CreateContent[SIZE];
            for (int i = 0; i < 15; ++i) {
                ContentId contentId = new ContentId();
                contents[i] = new CreateContent(contentId, new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), new Pet("Maya", "Cocker Spaniel", GenderEnum.FEMALE, SightingEnum.DOG));
            }
            for (int i = 15; i < SIZE; ++i) {
                ContentId contentId = new ContentId();
                contents[i] = new CreateContent(contentId, new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), new Keys("Square Key", SightingEnum.KEYS));
            }

            testFixture.givenCommands(contents)
                    .whenQuery(new GetContents(Collections.emptyList(), null, new Pagination(0, 10)))
                    .expectResult(hasSize(10))
                    .andThen()
                    .whenQuery(new GetContents(Collections.emptyList(), null, new Pagination(1, 10)))
                    .expectResult(hasSize(10))
                    .andThen()
                    .whenQuery(new GetContents(Collections.emptyList(), null, new Pagination(2, 10)))
                    .expectResult(hasSize(5))
                    .andThen()
                    .whenQuery(new GetContents(Collections.emptyList(), null, new Pagination(0, 30)))
                    .expectResult(hasSize(25))
                    .andThen()
                    .whenQuery(new GetFacets(new GetContents(Collections.emptyList(), null, new Pagination(0, 10))))
                    .expectResult(Objects::nonNull)
                    .mapResult(GetFacetStatsResult::getStats)
                    .expectResult(facetStats -> facetStats.size() == 2 &&
                            facetStats.stream().filter(it -> it.getValue().equals("pet")).findFirst().orElseThrow(() -> new AssertionError("No pet facet found")).getCount() == 15 &&
                            facetStats.stream().filter(it -> it.getValue().equals("keys")).findFirst().orElseThrow(() -> new AssertionError("No keys facet found")).getCount() == 10);
        }
    }

    @Nested
    class ContentIntegrationTests {
        @Test
        void deleteContent() {
            testFixture.givenCommands("create-content.json")
                    .whenQuery(new GetContents(Collections.emptyList(), null, new Pagination(0, 10)))
                    .expectResult(hasSize(1))
                    .andThen()
                    .whenCommand("delete-content.json")
                    .expectEvents("delete-content.json")
                    .andThen()
                    .whenQuery(new GetContents(Collections.emptyList(), null, new Pagination(0, 10)))
                    .expectResult(List::isEmpty);
        }
    }

    @Nested
    class ContentSchedulerTests {
        @BeforeEach
        void setUp() {
            testFixture.registerHandlers(new ContentScheduler())
                    .withClock(Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.systemDefault())).givenCommands("create-content.json");
        }

        @Test
        void createContentDetails() {
            testFixture
                    .whenCommand("publish-content.json")
                    .expectNoErrors()
                    .expectEvents("publish-content.json")
                    .andThen()
                    .whenTimeElapses(Duration.ofDays(90))
                    .expectEvents(TakeContentOffline.class);
        }

        @Test
        void documentTimestampIsUpdated() {
            testFixture
                    .whenQuery(new GetContents(Collections.emptyList(), null, new Pagination(0, 10)))
                    .mapResult(List::getFirst)
                    .mapResult(ContentDocument::timestamp)
                    .expectResult(timestamp -> withSlack("2025-01-01T00:00:00Z", timestamp))
                    .andThen()
                    .whenTimeElapses(Duration.ofDays(1))
                    .andThen()
                    .givenCommands("publish-content.json")
                    .whenQuery(new GetContents(Collections.emptyList(), null, new Pagination(0, 10)))
                    .mapResult(List::getFirst)
                    .mapResult(ContentDocument::timestamp)
                    .expectResult(timestamp -> withSlack("2025-01-02T00:00:00Z", timestamp));
        }
    }

    boolean withSlack(String expectedString, Instant actual) {
        Duration margin = Duration.ofSeconds(2);
        Instant expected = Instant.parse(expectedString);
        return Duration.between(expected, actual).abs().compareTo(margin) <= 0;
    }
}