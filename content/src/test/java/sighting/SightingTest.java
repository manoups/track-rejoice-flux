package sighting;

import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.PublishContent;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingDetails;
import com.breece.sighting.api.model.SightingId;
import com.breece.content.ContentErrors;
import com.breece.content.api.model.SightingState;
import com.breece.content.command.api.ClaimSighting;
import com.breece.content.command.api.ContentHandler;
import com.breece.content.command.api.SightingHandler;
import com.breece.sighting.command.api.DeleteSighting;
import com.breece.sighting.command.api.LinkSightingBackToContent;
import com.breece.sighting.query.api.GetSighting;
import com.breece.sighting.query.api.GetSightings;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import util.TestUtilities;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

public class SightingTest extends TestUtilities {

    final TestFixture testFixture = TestFixture.create(ContentHandler.class, SightingHandler.class, SightingState.class)
            .givenCommands(createUserFromProfile(viewer), createUserFromProfile(user2), createUserFromProfile(Alice));


    @Nested
    class SightingQueryTestsWithoutRemovalEnabled {
        @BeforeEach
        void setUp() {
            testFixture.givenCommandsByUser("viewer", "../content/create-content.json", "create-sighting.json")
                    .givenCommands("../content/publish-content.json");
        }

        @Test
        void claimSightingWithCorrectContentOwner() {
            testFixture
                    .whenCommandByUser("viewer", "claim-sighting.json")
                    .expectNoErrors()
                    .expectEvents("claim-sighting.json")
                    .expectCommands(LinkSightingBackToContent.class)
                    .andThen()
                    .whenQuery(new GetSighting(new SightingId("1")))
                    .expectResult(Objects::nonNull)
                    .mapResult(Sighting::linkedContents)
                    .expectResult(hasSize(1));
        }

        @Test
        void claimSightingForContentOfOtherUser() {
            testFixture.whenCommandByUser("Alice", "claim-sighting.json")
                    .expectExceptionalResult(ContentErrors.unauthorized)
                    .expectError((e) -> e.getMessage().equals(ContentErrors.unauthorized.getMessage()));
        }

        @Test
        void claimAlreadyClaimedSighting() {
            testFixture.givenCommandsByUser("viewer", "claim-sighting.json")
                    .givenCommandsByUser("user2", "../content/create-content-keys.json")
                    .givenCommands(new PublishContent(new ContentId("2"), Duration.ofDays(90)))
                    .whenCommandByUser("user2", new ClaimSighting(new ContentId("2"), new SightingId("1"), new SightingDetails(
                            new BigDecimal("78.901"), new BigDecimal("123.456")
                    )))
                    .expectNoErrors();
        }

        @Test
        void givenSighting_whenClaimSightingWithInvertedCoords_thenError() {
            testFixture
                    .whenCommand(new ClaimSighting(new ContentId("1"), new SightingId("1"), new SightingDetails(
                            new BigDecimal("123.456"), new BigDecimal("78.901")
                    )))
                    .expectExceptionalResult(SightingErrors.sightingMismatch)
                    .expectError((e) -> e.getMessage().equals(SightingErrors.sightingMismatch.getMessage()));
        }

        @Test
        void givenSighting_whenClaimSightingWithExtraDecimals_thenNoError() {
            testFixture
                    .whenCommand(new ClaimSighting(new ContentId("1"), new SightingId("1"), new SightingDetails(
                            new BigDecimal("78.901000"), new BigDecimal("123.456000")
                    )))
                    .expectNoErrors();
        }
    }

    @Nested
    class SightingQueryTestsWithRemovalEnabled {
        @BeforeEach
        void setUp() {
            testFixture.givenCommandsByUser("viewer",
                            "../content/create-content.json", "create-sighting-removal.json")
                    .givenCommands("../content/publish-content.json");
        }

        @Test
        void claimAlreadyClaimedSightingWithRemovalEnabled() {
            testFixture
                    .givenCommandsByUser("viewer", "claim-sighting.json")
                    .givenCommandsByUser("user2", "../content/create-content-keys.json")
                    .givenCommands(new PublishContent(new ContentId("2"), Duration.ofDays(90)))
                    .whenCommandByUser("user2", new ClaimSighting(new ContentId("2"), new SightingId("1"), new SightingDetails(
                            new BigDecimal("78.901"), new BigDecimal("123.456")
                    )))
                    .expectExceptionalResult(SightingErrors.notFound)
                    .expectError((e) -> e.getMessage().equals(SightingErrors.notFound.getMessage()));
        }

        @Test
        void givenSightingClaimed_whenGetSightingsWithRemovalEnabled_thenNoResults() {
            testFixture.givenCommandsByUser("viewer", "claim-sighting.json")
                    .whenQuery(new GetSightings())
                    .expectResult(List::isEmpty);
        }

        @Test
        void givenSightingClaimedWithRemovalEnabled_whenGetSightings_thenNoResults() {
            testFixture.whenCommandByUser("viewer", "claim-sighting.json")
                    .expectNoErrors()
                    .expectEvents("claim-sighting.json", LinkSightingBackToContent.class, DeleteSighting.class)
                    .andThen()
                    .whenQuery(new GetSightings())
                    .expectResult(List::isEmpty);
        }
    }

}
