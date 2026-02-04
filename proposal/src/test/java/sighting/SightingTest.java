package sighting;

import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.PublishContent;
import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.api.*;
import com.breece.proposal.api.model.LinkedSightingId;
import com.breece.proposal.api.model.LinkedSightingState;
import com.breece.proposal.api.model.LinkedSightingStatus;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.DeleteSighting;
import com.breece.sighting.query.api.GetSightings;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import util.TestUtilities;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

public class SightingTest extends TestUtilities {

    final TestFixture testFixture = TestFixture.create(LinkedSightingState.class)
            .givenCommands(createUserFromProfile(viewer), createUserFromProfile(user2), createUserFromProfile(Alice));


    @Nested
    class SightingQueryTestsWithoutRemovalEnabled {
        @BeforeEach
        void setUp() {
            testFixture.givenCommandsByUser("viewer", "../content/create-content.json", "create-sighting.json")
                    .givenCommands("../content/publish-content.json");
        }

        @Test
        void claimSightingForContentOfOtherUser() {
            testFixture.whenCommandByUser("Alice", "claim-sighting.json")
                    .expectExceptionalResult(LinkedSightingErrors.unauthorized)
                    .expectError((e) -> e.getMessage().equals(LinkedSightingErrors.unauthorized.getMessage()));
        }

        @Test
        void claimAlreadyClaimedSighting() {
            ContentId contentId = new ContentId("2");
            SightingId sightingId = new SightingId("1");
            testFixture.givenCommandsByUser("viewer", "claim-sighting.json")
                    .givenCommandsByUser("user2", "../content/create-content-keys.json")
                    .givenCommands(new PublishContent(contentId, Duration.ofDays(90)))
                    .whenCommandByUser("user2", new ClaimSighting(contentId, sightingId, new SightingDetails(
                            new BigDecimal("78.901"), new BigDecimal("123.456")
                    ), new LinkedSightingId(contentId, sightingId)))
                    .expectNoErrors();
        }

        @Test
        void givenSighting_whenClaimSightingWithInvertedCoords_thenError() {
            ContentId contentId = new ContentId("1");
            SightingId sightingId = new SightingId("1");
            testFixture
                    .whenCommand(new ClaimSighting(contentId, sightingId, new SightingDetails(
                            new BigDecimal("123.456"), new BigDecimal("78.901")
                    ), new LinkedSightingId(contentId, sightingId)))
                    .expectExceptionalResult(SightingErrors.sightingMismatch)
                    .expectError((e) -> e.getMessage().equals(SightingErrors.sightingMismatch.getMessage()));
        }

        @Test
        void givenSighting_whenClaimSightingWithExtraDecimals_thenNoError() {
            ContentId contentId = new ContentId("1");
            SightingId sightingId = new SightingId("1");
            testFixture
                    .whenCommand(new ClaimSighting(contentId, sightingId, new SightingDetails(
                            new BigDecimal("78.901000"), new BigDecimal("123.456000")),
                            new LinkedSightingId(contentId, sightingId)))
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
            ContentId contentId = new ContentId("2");
            SightingId sightingId = new SightingId("1");
            testFixture
                    .givenCommandsByUser("viewer", "claim-sighting-removal.json")
                    .givenCommandsByUser("user2", "../content/create-content-keys.json")
                    .givenCommands(new PublishContent(contentId, Duration.ofDays(90)))
                    .whenCommandByUser("user2", new ClaimSighting(contentId, sightingId, new SightingDetails(
                            new BigDecimal("78.901"), new BigDecimal("123.456")
                    ), new LinkedSightingId(contentId, sightingId)))
                    .expectExceptionalResult(SightingErrors.notFound)
                    .expectError((e) -> e.getMessage().equals(SightingErrors.notFound.getMessage()));
        }

        @Test
        void checkDiffInStates() {
            testFixture.givenCommandsByUser("Alice", "../proposal/create-proposal-removal.json")
                    .whenQuery(new GetLinkedSightingsBySightingIdAndStatuses(new SightingId("1"), List.of(LinkedSightingStatus.CREATED, LinkedSightingStatus.REJECTED)))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .expectResult(hasSize(1))
                    .andThen()
                    .whenCommandByUser("viewer", "claim-sighting-removal.json")
                    .expectNoErrors()
                    .expectCommands(DeleteSighting.class, UpdateLastSeenPosition.class)
                    .andThen()
                    .whenQuery(new GetLinkedSightingsBySightingIdAndStatuses(new SightingId("1"), List.of(LinkedSightingStatus.CREATED, LinkedSightingStatus.REJECTED)))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .expectResult(List::isEmpty);
        }

        @Test
        void givenSightingClaimedWithRemovalEnabled_whenGetSightings_thenNoResults() {
            testFixture.whenCommandByUser("viewer", "claim-sighting-removal.json")
                    .expectNoErrors()
                    .expectOnlyEvents(DeleteSighting.class, CreateProposal.class, UpdateLastSeenPosition.class, DeleteLinkedProposal.class, AcceptProposal.class)
                    .andThen()
                    .whenQuery(new GetSightings())
                    .expectResult(List::isEmpty);
        }
    }

}
