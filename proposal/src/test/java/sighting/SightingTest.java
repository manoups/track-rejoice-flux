package sighting;

import com.breece.content.ContentErrors;
import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.PublishContent;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.api.*;
import com.breece.proposal.api.model.LinkedSightingStatus;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.DeleteSighting;
import com.breece.sighting.command.api.LinkSightingBackToContent;
import com.breece.sighting.query.api.GetSightings;
import util.TestUtilities;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

public class SightingTest extends TestUtilities {

    final TestFixture testFixture = TestFixture.create(SightingHandler.class)
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
                    .whenQuery(new GetLinkedSightingsBySightingIdAndStatuses(new SightingId("1"), List.of(LinkedSightingStatus.CREATED)))
                    .expectResult(Objects::nonNull)
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
                    ), false, null))
                    .expectNoErrors();
        }

        @Test
        void givenSighting_whenClaimSightingWithInvertedCoords_thenError() {
            testFixture
                    .whenCommand(new ClaimSighting(new ContentId("1"), new SightingId("1"), new SightingDetails(
                            new BigDecimal("123.456"), new BigDecimal("78.901")
                    ),false, null))
                    .expectExceptionalResult(SightingErrors.sightingMismatch)
                    .expectError((e) -> e.getMessage().equals(SightingErrors.sightingMismatch.getMessage()));
        }

        @Test
        void givenSighting_whenClaimSightingWithExtraDecimals_thenNoError() {
            testFixture
                    .whenCommand(new ClaimSighting(new ContentId("1"), new SightingId("1"), new SightingDetails(
                            new BigDecimal("78.901000"), new BigDecimal("123.456000")),
                            false, null))
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
                    ), false, null))
                    .expectExceptionalResult(SightingErrors.notFound)
                    .expectError((e) -> e.getMessage().equals(SightingErrors.notFound.getMessage()));
        }

        @Test
        void givenSightingClaimed_whenGetSightingsWithRemovalEnabled_thenNoResults() {
            testFixture.givenCommandsByUser("viewer", "claim-sighting.json")
                    .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(LinkedSightingStatus.CREATED, LinkedSightingStatus.REJECTED)))
                    .expectResult(List::isEmpty);
        }

        @Test
        void givenSightingClaimedWithRemovalEnabled_whenGetSightings_thenNoResults() {
            testFixture.whenCommandByUser("viewer", "claim-sighting.json")
                    .expectNoErrors()
                    .expectEvents("claim-sighting.json", LinkSightingBackToContent.class, DeleteSighting.class, CreateProposal.class, AcceptProposal.class)
                    .andThen()
                    .whenQuery(new GetSightings())
                    .expectResult(List::isEmpty);
        }
    }

}
