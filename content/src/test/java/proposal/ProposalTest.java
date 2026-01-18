package proposal;

import com.breece.common.model.Content;
import com.breece.common.model.ContentId;
import com.breece.common.model.ProposedSightingId;
import com.breece.common.sighting.SightingErrors;
import com.breece.content.command.api.*;
import com.breece.content.command.api.handler.SightingHandler;
import com.breece.content.query.api.GetContent;
import com.breece.content.query.api.GetSightingHistoryForContent;
import com.breece.content.ContentErrors;
import com.breece.coreapi.util.GeometryUtil;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import util.TestUtilities;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

@Slf4j
public class ProposalTest extends TestUtilities {
    final TestFixture testFixture = TestFixture.create(ProposedSightingHandler.class, SightingHandler.class, ContentHandler.class);

    @Test
    void givenNoContent_whenProposalCreated_thenError() {
        testFixture.givenCommands("../sighting/create-sighting.json")
                .whenCommand("create-proposal.json")
                .expectError(ContentErrors.notFound);
    }

    @Test
    void createProposal() {
        testFixture.givenCommands("../content/create-content.json", "../sighting/create-sighting.json", "../content/publish-content.json")
                .whenCommand("create-proposal.json")
                .expectNoErrors()
                .expectEvents("create-proposal.json");
    }

    @Test
    void confirmCoords() {
        testFixture.givenCommands("../content/create-content.json")
                .whenQuery(new GetContent(new ContentId("1")))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .mapResult(Content::lastConfirmedSighting)
                .expectResult(Objects::nonNull)
                .expectResult(details -> GeometryUtil.parseLocation(details.lat(), details.lng()).within(GeometryUtil.parseLocation(0.0, 0.0)));
    }

    @Test
    void givenUnpublishedContent_whenCreateProposal_thenError() {
        testFixture.givenCommands("../sighting/create-sighting.json", "../content/create-content.json")
                .whenCommand("create-proposal.json")
                .expectNoEvents()
                .expectError(IllegalCommandException.class);
    }

    @Test
    void givenRejectProposal_whenQueryProposedSightings_thenEmptyList() {
        testFixture.givenCommands("../sighting/create-sighting.json", "../content/create-content.json",
                        "../content/publish-content.json", "create-proposal.json", "reject-proposal.json")
                .whenQuery(new GetContent(new ContentId("1")))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .mapResult(Content::proposedSightings)
                .expectResult(List::isEmpty);
    }

    @Test
    void givenContentOfUserA_whenUserBCreatesProposal_thenNoError() {
        testFixture.givenCommandsByUser(viewer, "../content/create-content.json")
                .givenCommandsByUser(user2, "../sighting/create-sighting.json")
                .givenCommands("../content/publish-content.json")
                .whenCommandByUser(user2, "create-proposal.json")
                .expectNoErrors()
                .expectEvents("create-proposal.json");
    }

    @Test
    void givenContentOfUserASightingOfUserB_whenUserAliceCreatesProposal_thenError() {
        testFixture.givenCommandsByUser(viewer, "../content/create-content.json")
                .givenCommandsByUser(user2, "../sighting/create-sighting.json")
                .whenCommandByUser(Alice, "create-proposal.json")
                .expectError(SightingErrors.notOwner);
    }

    @Test
    void givenContentOfUserA_whenUserBRemovesProposal_thenError() {
        testFixture.givenCommandsByUser(viewer, "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json")
                .givenCommandsByUser(viewer, "create-proposal.json")
                .whenCommandByUser(user2, "reject-proposal.json")
                .expectError(IllegalCommandException.class);
    }

    @Test
    void givenContentOfUserA_whenUserBAcceptsProposal_thenError() {
        testFixture.givenCommandsByUser(viewer, "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json")
                .givenCommandsByUser(viewer, "create-proposal.json")
                .whenCommandByUser(user2, "accept-proposal.json")
                .expectError(ContentErrors.unauthorized);
    }

    @Test
    void givenClaim_whenSameProposal_thenError() {
        testFixture.givenCommands("../sighting/create-sighting.json", "../content/create-content.json", "../content/publish-content.json", "../sighting/claim-sighting.json")
                .whenCommand("create-proposal.json")
                .expectError(SightingErrors.alreadyProposed);
    }

    @Nested
    class CreatePublishPropose {
        @BeforeEach
        void setUp() {
            testFixture.givenCommands("../content/create-content.json", "../sighting/create-sighting.json", "../content/publish-content.json", "create-proposal.json");
        }

        @Test
        void givenAcceptedProposal_whenQueryProposedSightings_thenEmptyList() {
            testFixture.whenQuery(new GetContent(new ContentId("1")))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .mapResult(Content::proposedSightings)
                    .expectResult(hasSize(1))
                    .andThen()
                    .givenCommands("accept-proposal.json")
                    .whenQuery(new GetContent(new ContentId("1")))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .mapResult(Content::proposedSightings)
                    .expectResult(List::isEmpty);
        }

        @Test
        void givenProposal_whenNonExistentProposalRejected_thenError() {
            testFixture.whenCommand(new RemoveMemberProposal(new ProposedSightingId("2")))
                    .expectError(ProposedSightingErrors.notFound);
        }

        @Test
        void rejectProposal() {
            testFixture.whenCommand("reject-proposal.json")
                    .expectNoErrors()
                    .expectEvents("reject-proposal.json");
        }

        @Test
        void givenProposedSighting_whenQueryContent_thenCoordsDoNotChange() {
            testFixture.whenQuery(new GetContent(new ContentId("1")))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .mapResult(Content::lastConfirmedSighting)
                    .expectResult(Objects::nonNull)
                    .expectResult(details -> GeometryUtil.parseLocation(details.lat(), details.lng()).within(GeometryUtil.parseLocation(0.0, 0.0)));
        }

        @Test
        void confirmProposalCommands() {
            testFixture.whenCommand("accept-proposal.json")
                    .expectNoErrors()
                    .expectEvents("accept-proposal.json");
        }

        @Test
        void confirmProposal() {
            testFixture
                    .givenCommands("accept-proposal.json")
                    .whenQuery(new GetContent(new ContentId("1")))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .mapResult(Content::lastConfirmedSighting)
                    .expectResult(Objects::nonNull)
                    .expectResult(details -> GeometryUtil.parseLocation(details.lat(), details.lng()).within(GeometryUtil.parseLocation(123.456, 78.901)));
        }

        @Test
        void givenSighting_whenCreateProposal_thenContentShouldContainTheProposal() {
            testFixture.whenQuery(new GetContent(new ContentId("1")))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .mapResult(Content::proposedSightings)
                    .expectResult(hasSize(1));
        }

        @Test
        void givenProposal_whenSameProposal_thenError() {
            testFixture.whenCommand("create-proposal.json")
                    .expectError(SightingErrors.alreadyProposed);
        }
        @Test
        void givenAProposal_whenDeleteSighting_thenProposalRemoved() {
            testFixture.whenCommand("../sighting/delete-sighting.json")
                    .expectNoErrors()
                    .expectEvents("../sighting/delete-sighting.json")
                    .expectCommands(RemoveMemberProposal.class)
                    .andThen()
                    .whenQuery(new GetContent(new ContentId("1")))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .mapResult(Content::proposedSightings)
                    .expectResult(List::isEmpty);
        }

        @Test
        void givenAnAcceptedProposal_whenDeleteSighting_thenProposalUnaffected() {
            testFixture.givenCommands("accept-proposal.json")
                    .whenCommand("../sighting/delete-sighting.json")
                    .expectNoErrors()
                    .expectEvents("../sighting/delete-sighting.json")
                    .expectNoCommands()
                    .andThen()
                    .whenQuery(new GetContent(new ContentId("1")))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .mapResult(Content::lastConfirmedSighting)
                    .expectResult(Objects::nonNull);
        }

        @Test
        void givenAProposal_whenClaim_thenProposalUnaffected() {
            testFixture
                    .whenCommand("../sighting/claim-sighting.json")
                    .expectNoErrors()
                    .expectCommands(RemoveMemberProposal.class)
                    .andThen()
                    .whenQuery(new GetContent(new ContentId("1")))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .mapResult(Content::proposedSightings)
                    .expectResult(List::isEmpty);
        }

        @Test
        void history() {
            testFixture.givenCommands("accept-proposal.json")
                    .whenQuery(new GetSightingHistoryForContent(new ContentId("1")))
                    .expectResult(hasSize(2));
        }
    }
}
