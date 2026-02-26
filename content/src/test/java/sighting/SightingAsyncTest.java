package sighting;

import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.proposal.command.api.AcceptProposal;
import com.breece.proposal.command.api.GetLinkedSightingsByContentIdAndStatuses;
import com.breece.proposal.command.api.model.WeightedAssociationIdState;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;
import util.TestUtilities;

import java.util.List;

public class SightingAsyncTest extends TestUtilities {
    final TestFixture testFixture = TestFixture.createAsync(WeightedAssociationIdState.class)
            .givenCommands(createUserFromProfile(viewer), createUserFromProfile(user2), createUserFromProfile(Alice));


    @Test
    void givenSightingClaimed_whenGetSightingsWithRemovalEnabled_thenNoResults() {
        testFixture.givenCommandsByUser("viewer",
                        "../content/create-content.json", "create-sighting-removal.json")
                .givenCommands("../content/publish-content.json")
                .givenCommandsByUser("viewer", "claim-sighting-removal.json")
                .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.CREATED, WeightedAssociationStatus.REJECTED)))
                .expectResult(List::isEmpty);
    }

//    @Disabled("GetLinkedSightingsByContentIdAndStatuses should be disabled")
    @Test
    void claimSightingWithCorrectContentOwner() {
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json", "create-sighting.json")
                .givenCommands("../content/publish-content.json")
                .whenCommandByUser("viewer", "claim-sighting.json")
                .expectNoErrors()
                .expectOnlyEvents(com.breece.proposal.command.api.CreateProposal.class, AcceptProposal.class, UpdateLastSeenPosition.class)
                .expectCommands(UpdateLastSeenPosition.class);
//                .andThen()
//                .whenQuery(new GetLinkedSightingsBySightingIdAndStatuses(new SightingId("1"), List.of(LinkedSightingStatus.ACCEPTED)))
//                .expectResult(Objects::nonNull)
//                .expectResult(hasSize(1));
    }
}
