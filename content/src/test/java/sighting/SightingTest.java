package sighting;

import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.PublishContent;
import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.coreapi.facets.Pagination;
import com.breece.proposal.command.api.*;
import com.breece.proposal.command.api.model.WeightedAssociationHandler;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationState;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.DeleteSighting;
import com.breece.sighting.query.api.GetSightings;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import util.TestUtilities;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class SightingTest extends TestUtilities {

    final TestFixture testFixture = TestFixture.create(WeightedAssociationState.class, new WeightedAssociationHandler())
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
                    .expectNoCommands()
                    .expectOnlyEvents(CreateProposal.class)
                    .expectNoErrors();
        }

        @Test
        void claimAlreadyClaimedSighting() {
            ContentId contentId = new ContentId("3");
            SightingId sightingId = new SightingId("1");
            testFixture.givenCommandsByUser("viewer", "claim-sighting.json")
                    .givenCommandsByUser("user2", "../content/create-content-cat.json")
                    .givenCommands(new PublishContent(contentId, Duration.ofDays(90)))
                    .whenCommandByUser("user2", new ClaimSighting(contentId,
                            new WeightedAssociationId(contentId, sightingId)))
                    .expectNoErrors();
        }

        @Test
        @Disabled
        void givenSighting_whenClaimSightingWithInvertedCoords_thenError() {
            ContentId contentId = new ContentId("1");
            SightingId sightingId = new SightingId("1");
            testFixture
                    .whenCommand(new com.breece.proposal.command.api.ClaimSighting(contentId, new WeightedAssociationId(contentId, sightingId)))
                    .expectExceptionalResult(SightingErrors.sightingMismatch)
                    .expectError((e) -> e.getMessage().equals(SightingErrors.sightingMismatch.getMessage()));
        }

        @Test
        @Disabled
        void givenSighting_whenClaimSightingWithExtraDecimals_thenNoError() {
            ContentId contentId = new ContentId("1");
            SightingId sightingId = new SightingId("1");
            testFixture
                    .whenCommand(new com.breece.proposal.command.api.ClaimSighting(contentId,
                            new WeightedAssociationId(contentId, sightingId)))
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
            ContentId contentId = new ContentId("3");
            SightingId sightingId = new SightingId("1");
            testFixture
                    .givenCommandsByUser("viewer", "claim-sighting-removal.json")
                    .givenCommandsByUser("user2", "../content/create-content-cat.json")
                    .givenCommands(new PublishContent(contentId, Duration.ofDays(90)))
                    .whenCommandByUser("user2", new ClaimSighting(contentId,
                            new WeightedAssociationId(contentId, sightingId)))
                    .expectExceptionalResult(WeightedAssociationErrors.notFound)
                    .expectError((e) -> e.getMessage().equals(WeightedAssociationErrors.notFound.getMessage()));
        }

        @Test
        void checkDiffInStates() {
            testFixture.givenCommandsByUser("Alice", "../proposal/create-proposal-removal.json")
                    .whenCommandByUser("viewer", "claim-sighting-removal.json")
                    .expectNoErrors()
                    .expectCommands(DeleteSighting.class, UpdateLastSeenPosition.class);
        }

        @Test
        void givenSightingClaimedWithRemovalEnabled_whenGetSightings_thenNoResults() {
            testFixture.whenCommandByUser("viewer", "claim-sighting-removal.json")
                    .expectNoErrors()
                    .expectOnlyEvents(CreateProposal.class, AcceptProposal.class, DeleteSighting.class, UpdateLastSeenPosition.class, DeleteLinkedProposal.class)
                    .andThen()
                    .whenQuery(new GetSightings(Collections.emptyList(),null, new Pagination(0,10)))
                    .expectResult(List::isEmpty);
        }
    }
}
