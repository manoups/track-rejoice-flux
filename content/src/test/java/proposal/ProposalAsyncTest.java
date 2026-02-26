package proposal;

import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.PublishContent;
import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.command.api.DeleteLinkedProposal;
import com.breece.proposal.command.api.GetLinkedSightingsByContentIdAndStatuses;
import com.breece.proposal.command.api.model.WeightedAssociation;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.LinkedSightingState;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.DeleteSighting;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;
import util.TestUtilities;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ProposalAsyncTest extends TestUtilities {
    final TestFixture testFixture = TestFixture.createAsync(LinkedSightingState.class).givenCommands(createUserFromProfile(viewer), createUserFromProfile(user2), createUserFromProfile(Alice));

    @Test
    void givenCreateProposalForRemovableSightingContent2_whenClaimSightingForContent1_thenStateOfContent2ShouldBeDeleted() {
        ContentId contentId = new ContentId("2");
        SightingId sightingId = new SightingId("1");
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json").givenCommands("../content/publish-content.json")
                .givenCommandsByUser("Alice", "../sighting/create-sighting-removal.json", "create-proposal.json")
//            create C2
                .givenCommandsByUser("viewer", "../content/create-content-keys.json")
                .givenCommands(new PublishContent(contentId, Duration.ofDays(10)))
                .whenCommandByUser("viewer", new com.breece.proposal.command.api.ClaimSighting(contentId, sightingId, new SightingDetails(new BigDecimal("78.901"),
                        new BigDecimal("123.456")),
                        new WeightedAssociationId(contentId, sightingId)
                ))
                .expectOnlyCommands(UpdateLastSeenPosition.class, DeleteSighting.class, DeleteLinkedProposal.class, DeleteLinkedProposal.class)
                .expectThat(fz -> {
                    List<LinkedSightingState> linkedSightingStates = fz.documentStore().search(LinkedSightingState.class).fetchAll();
                    assertThat(linkedSightingStates).isEmpty();
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
        ContentId contentId = new ContentId("2");
        SightingId sightingId = new SightingId("1");
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json").givenCommands("../content/publish-content.json")
                .givenCommandsByUser("Alice", "../sighting/create-sighting.json", "create-proposal.json")
//            create C2
                .givenCommandsByUser("viewer", "../content/create-content-keys.json")
                .givenCommands(new PublishContent(contentId, Duration.ofDays(10)))
                .whenCommandByUser("viewer", new com.breece.proposal.command.api.ClaimSighting(contentId, sightingId, new SightingDetails(new BigDecimal("78.901"),
                        new BigDecimal("123.456")),
                        new WeightedAssociationId(contentId, sightingId)
                ))
                .expectOnlyCommands(UpdateLastSeenPosition.class)
                .expectThat(fz -> {
                    List<LinkedSightingState> linkedSightingStates = fz.documentStore().search(LinkedSightingState.class).fetchAll();
                    assertThat(linkedSightingStates).hasSize(2);
                })
                .andThen()
                .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.CREATED)))
                .expectResult(hasSize(1))
                .andThen()
                .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(contentId, List.of(WeightedAssociationStatus.ACCEPTED)))
                .expectResult(hasSize(1));
    }
}
