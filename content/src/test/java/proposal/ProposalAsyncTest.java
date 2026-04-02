package proposal;

import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.PublishContent;
import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.proposal.command.api.ClaimSighting;
import com.breece.proposal.command.api.DeleteWeightedAssociation;
import com.breece.proposal.command.api.GetWeightedAssociationsByContentIdAndStatuses;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationState;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
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
    final TestFixture testFixture = TestFixture.createAsync(WeightedAssociationState.class, ContentState.class).givenCommands(createUserFromProfile(viewer), createUserFromProfile(user2), createUserFromProfile(Alice));

    @Test
    void givenCreateProposalForRemovableSightingContent2_whenClaimSightingForContent1_thenStateOfContent2ShouldBeDeleted() {
        ContentId contentId = new ContentId("3");
        SightingId sightingId = new SightingId("1");
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json").givenCommands("../content/publish-content.json")
                .givenCommandsByUser("Alice", "../sighting/create-sighting-removal.json")
                .givenCommands("create-weighted-association.json")
                .givenCommandsByUser("Alice","create-proposal.json")
//            create C2
                .givenCommandsByUser("viewer", "../content/create-content-cat.json")
                .givenCommands(new PublishContent(contentId, Duration.ofDays(10)))
                .givenCommands("create-weighted-association-for-content-3.json")
                .whenCommandByUser("viewer", new ClaimSighting(contentId,
                        new WeightedAssociationId(String.format("%s-%s", contentId, sightingId))
                ))
                .expectNoErrors()
                .expectOnlyCommands(UpdateLastSeenPosition.class, DeleteSighting.class, DeleteWeightedAssociation.class, DeleteWeightedAssociation.class)
                .expectThat(fz -> {
                    List<WeightedAssociationState> weightedAssociationStates = fz.documentStore().search(WeightedAssociationState.class).fetchAll();
                    assertThat(weightedAssociationStates).isEmpty();
                })
                .andThen()
                .whenQuery(new GetWeightedAssociationsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.CREATED)))
                .expectResult(List::isEmpty)
                .andThen()
                .whenQuery(new GetWeightedAssociationsByContentIdAndStatuses(contentId, List.of(WeightedAssociationStatus.ACCEPTED)))
                .expectResult(List::isEmpty);
    }

    @Test
    void givenCreateProposalForContent2_whenClaimSightingForContent1_thenStateOfContent2ShouldNotBeChanged() {
        ContentId contentId = new ContentId("3");
        SightingId sightingId = new SightingId("1");
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json")
                .givenCommands("../content/publish-content.json")
                .givenCommandsByUser("Alice", "../sighting/create-sighting.json")
                .givenCommands("create-weighted-association.json")
                .givenCommandsByUser("Alice", "create-proposal.json")
//            create C2
                .givenCommandsByUser("viewer", "../content/create-content-cat.json")
                .givenCommands("create-weighted-association-for-content-3.json")
                .givenCommands(new PublishContent(contentId, Duration.ofDays(10)))
                .whenCommandByUser("viewer", new ClaimSighting(contentId,
                        new WeightedAssociationId("content-3-sighting-1")
                ))
                .expectOnlyCommands(UpdateLastSeenPosition.class)
                .expectThat(fz -> {
                    List<WeightedAssociationState> weightedAssociationStates = fz.documentStore().search(WeightedAssociationState.class).fetchAll();
                    assertThat(weightedAssociationStates).hasSize(2);
                })
                .andThen()
                .whenQuery(new GetWeightedAssociationsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.LINKED)))
                .expectResult(hasSize(1))
                .andThen()
                .whenQuery(new GetWeightedAssociationsByContentIdAndStatuses(contentId, List.of(WeightedAssociationStatus.ACCEPTED)))
                .expectResult(hasSize(1));
    }
}
