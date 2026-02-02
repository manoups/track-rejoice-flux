package sighting;

import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.proposal.api.*;
import com.breece.proposal.api.model.LinkedSightingState;
import com.breece.proposal.api.model.LinkedSightingStatus;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;
import util.TestUtilities;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

public class SightingAsyncTest extends TestUtilities {
    final TestFixture testFixture = TestFixture.createAsync(LinkedSightingState.class)
            .givenCommands(createUserFromProfile(viewer), createUserFromProfile(user2), createUserFromProfile(Alice));


    @Test
    void givenSightingClaimed_whenGetSightingsWithRemovalEnabled_thenNoResults() {
        testFixture.givenCommandsByUser("viewer",
                        "../content/create-content.json", "create-sighting-removal.json")
                .givenCommands("../content/publish-content.json")
                .givenCommandsByUser("viewer", "claim-sighting-removal.json")
                .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(LinkedSightingStatus.CREATED, LinkedSightingStatus.REJECTED)))
                .expectResult(List::isEmpty);
    }

    @Test
    void claimSightingWithCorrectContentOwner() {
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json", "create-sighting.json")
                .givenCommands("../content/publish-content.json")
                .whenCommandByUser("viewer", "claim-sighting.json")
                .expectNoErrors()
                .expectOnlyEvents(CreateProposal.class, AcceptProposal.class, UpdateLastSeenPosition.class, UpdateStatusProjection.class)
                .expectCommands(AcceptProposal.class, UpdateLastSeenPosition.class)
                .andThen()
                .whenQuery(new GetLinkedSightingsBySightingIdAndStatuses(new SightingId("1"), List.of(LinkedSightingStatus.ACCEPTED)))
                .expectResult(Objects::nonNull)
                .expectResult(hasSize(1));
    }
}
