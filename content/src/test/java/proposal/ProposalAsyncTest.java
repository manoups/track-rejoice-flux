package proposal;

import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.PublishContent;
import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.proposal.command.api.ClaimSighting;
import com.breece.proposal.command.api.DeleteLinkedProposal;
import com.breece.proposal.command.api.GetLinkedSightingsByContentIdAndStatuses;
import com.breece.proposal.command.api.model.*;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.DeleteSighting;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;
import util.TestUtilities;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ProposalAsyncTest extends TestUtilities {
    final TestFixture testFixture = TestFixture.createAsync(WeightedAssociationState.class, ContentState.class, new WeightedAssociationHandler()).givenCommands(createUserFromProfile(viewer), createUserFromProfile(user2), createUserFromProfile(Alice));

    @Test
    void givenCreateProposalForRemovableSightingContent2_whenClaimSightingForContent1_thenStateOfContent2ShouldBeDeleted() {
        ContentId contentId = new ContentId("3");
        SightingId sightingId = new SightingId("1");
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json").givenCommands("../content/publish-content.json")
                .givenCommandsByUser("Alice", "../sighting/create-sighting-removal.json", "create-proposal.json")
//            create C2
                .givenCommandsByUser("viewer", "../content/create-content-cat.json")
                .givenCommands(new PublishContent(contentId, Duration.ofDays(10)))
                .whenCommandByUser("viewer", new ClaimSighting(contentId,
                        new WeightedAssociationId(contentId, sightingId)
                ))
                .expectOnlyCommands(UpdateLastSeenPosition.class, DeleteSighting.class, DeleteLinkedProposal.class, DeleteLinkedProposal.class)
                .expectThat(fz -> {
                    List<WeightedAssociationState> weightedAssociationStates = fz.documentStore().search(WeightedAssociationState.class).fetchAll();
                    assertThat(weightedAssociationStates).isEmpty();
                    List<WeightedAssociation> weightedAssociations = fz.documentStore().search(WeightedAssociation.class).fetchAll();
                    assertThat(weightedAssociations).isEmpty();
                })
                .andThen()
                .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.CREATED)))
                .expectResult(List::isEmpty)
                .andThen()
                .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(contentId, List.of(WeightedAssociationStatus.ACCEPTED)))
                .expectResult(List::isEmpty);
    }

    @Test
    void givenCreateProposalForContent2_whenClaimSightingForContent1_thenStateOfContent2ShouldNotBeChanged() {
        ContentId contentId = new ContentId("3");
        SightingId sightingId = new SightingId("1");
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json")
                .givenCommands("../content/publish-content.json")
                .givenCommandsByUser("Alice", "../sighting/create-sighting.json", "create-proposal.json")
//            create C2
                .givenCommandsByUser("viewer", "../content/create-content-cat.json")
                .givenCommands(new PublishContent(contentId, Duration.ofDays(10)))
                .whenCommandByUser("viewer", new ClaimSighting(contentId,
                        new WeightedAssociationId(contentId, sightingId)
                ))
                .expectOnlyCommands(UpdateLastSeenPosition.class)
                .expectThat(fz -> {
                    List<WeightedAssociationState> weightedAssociationStates = fz.documentStore().search(WeightedAssociationState.class).fetchAll();
                    assertThat(weightedAssociationStates).hasSize(2);
                })
                .andThen()
                .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.LINKED)))
                .expectResult(hasSize(1))
                .andThen()
                .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(contentId, List.of(WeightedAssociationStatus.ACCEPTED)))
                .expectResult(hasSize(1));
    }
}
