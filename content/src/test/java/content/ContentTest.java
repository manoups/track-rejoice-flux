package content;

import com.breece.common.model.ContentId;
import com.breece.common.model.GenderEnum;
import com.breece.common.model.Keys;
import com.breece.common.model.Pet;
import com.breece.common.sighting.model.SightingDetails;
import com.breece.content.command.api.ContentScheduler;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.CreateContent;
import com.breece.content.command.api.TakeContentOffline;
import com.breece.content.query.api.GetContentStats;
import com.breece.content.query.api.GetContents;
import com.breece.content.ContentErrors;
import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserProfile;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;

class ContentTest {
    final TestFixture testFixture = TestFixture.create(ContentState.class);

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
        testFixture.givenCommandsByUser(new UserProfile(new UserId("viewer"), null, null),"create-content.json")
                .whenCommandByUser(new UserProfile(new UserId("user2"), null, null),"delete-content.json")
                .expectExceptionalResult(ContentErrors.unauthorized);
    }

    @Nested
    class ContentQueryTests {
        @Test
        void searchForContent() {
            testFixture.givenCommands("create-content.json")
                    .whenQuery(new GetContents())
                    .expectResult(hasSize(1));
        }

        @Test
        void searchPaginatedContent() {
            final int SIZE = 25;
            CreateContent[] contents = new CreateContent[SIZE];
            for(int i=0; i< 15; ++i) {
                ContentId contentId = new ContentId();
                contents[i] = new CreateContent(contentId, new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), new Pet("Maya", "Cocker Spaniel", GenderEnum.FEMALE));
            }
            for(int i=15; i< SIZE; ++i) {
                ContentId contentId = new ContentId();
                contents[i] = new CreateContent(contentId, new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), new Keys("Square Key"));
            }

            testFixture.givenCommands(contents)
                    .whenQuery(new GetContents())
                    .expectResult(hasSize(10))
                    .andThen()
                    .whenQuery(new GetContents(1, 10))
                    .expectResult(hasSize(10))
                    .andThen()
                    .whenQuery(new GetContents(2, 10))
                    .expectResult(hasSize(5))
                    .andThen()
                    .whenQuery(new GetContents(0, 30))
                    .expectResult(hasSize(25))
                    .andThen()
                    .whenQuery(new GetContentStats())
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
                .whenQuery(new GetContents())
                .expectResult(hasSize(1))
                .andThen()
                    .whenCommand("delete-content.json")
                    .expectEvents("delete-content.json")
                .andThen()
                .whenQuery(new GetContents())
                .expectResult(List::isEmpty);
        }
    }

    @Nested
    class ContentSchedulerTests {
        @BeforeEach
        void setUp() {
            testFixture.registerHandlers(new ContentScheduler()).givenCommands("create-content.json");
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
    }
}