package proposal;

import com.breece.content.ContentErrors;
import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.content.query.api.GetContent;
import com.breece.content.query.api.GetSightingHistoryForContent;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.util.GeometryUtil;
import com.breece.proposal.api.*;
import com.breece.proposal.api.model.LinkedSightingId;
import com.breece.proposal.api.model.LinkedSightingState;
import com.breece.proposal.api.model.LinkedSightingStatus;
import com.breece.sighting.command.api.CreateSighting;
import org.junit.jupiter.api.Disabled;
import util.TestUtilities;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

@Slf4j
public class ProposalTest extends TestUtilities {
    final TestFixture testFixture = TestFixture.create(LinkedSightingHandler.class).givenCommands(createUserFromProfile(viewer), createUserFromProfile(user2), createUserFromProfile(Alice));

    @Test
    void givenNoContent_whenProposalCreated_thenError() {
        testFixture.givenCommands("../sighting/create-sighting.json")
                .whenCommand("create-proposal.json")
                .expectError(ContentErrors.notFound);
    }

    @Test
    void createProposal() {
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json", "../sighting/create-sighting.json")
                .givenCommands("../content/publish-content.json")
                .whenCommand("create-proposal.json")
                .expectNoErrors()
                .expectEvents("create-proposal.json");
    }

    @Test
    void givenIncorrectLinkedSightingId_whenCreateProposal_thenError() {
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json", "../sighting/create-sighting.json")
                .givenCommands("../content/publish-content.json")
                .whenCommand(new CreateProposal(new ContentId("1"), new UserId("viewer"), new SightingId("1"), new LinkedSightingId(new ContentId("3"), new SightingId("2")),
                        new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), false))
                .expectError(LinkedSightingErrors.malformedKey)
                .expectExceptionalResult((e) -> e.getMessage().equals(LinkedSightingErrors.malformedKey.getMessage()));
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
    void givenProposal_whenLinkedProposalByDifferentUser_thenEmptyList() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json")
                .whenCommandByUser("Alice", "create-proposal.json")
                .expectNoErrors()
                .expectCommands()
                .expectEvents("create-proposal.json");
    }

    @Test
    void ProposalShouldBeLinkedToContentIfUserMatch() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json")
                .whenCommandByUser("viewer", "create-proposal.json")
                .expectNoErrors()
                .expectOnlyCommands(AcceptProposal.class, UpdateLastSeenPosition.class)
                .expectEvents("create-proposal.json", AcceptProposal.class, UpdateLastSeenPosition.class);
    }

    @Test
    void givenRejectProposal_whenQueryProposedSightings_thenEmptyList() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json")
                .givenCommandsByUser("Alice", "create-proposal.json")
                .givenCommandsByUser("viewer", "reject-proposal.json")
                .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(LinkedSightingStatus.CREATED)))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .expectResult(List::isEmpty);
    }

    @Test
    void givenContentOfUserA_whenUserBCreatesProposal_thenNoError() {
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json")
                .givenCommandsByUser("user2", "../sighting/create-sighting.json")
                .givenCommands("../content/publish-content.json")
                .whenCommandByUser("user2", "create-proposal.json")
                .expectNoErrors()
                .expectOnlyEvents("create-proposal.json");
    }

    @Disabled("Anyone should be able to create proposal")
    @Test
    void givenContentOfUserASightingOfUserB_whenUserAliceCreatesProposal_thenError() {
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json")
                .givenCommandsByUser("user2", "../sighting/create-sighting.json")
                .whenCommandByUser("Alice", "create-proposal.json")
                .expectError(SightingErrors.notOwner);
    }

    @Test
    void givenContentOfUserA_whenUserBRemovesProposal_thenError() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json")
                .givenCommandsByUser("viewer", "create-proposal.json")
                .whenCommandByUser("user2", "reject-proposal.json")
                .expectError(IllegalCommandException.class);
    }

    @Test
    void givenContentOfUserA_whenUserBAcceptsProposal_thenError() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json")
                .givenCommandsByUser("viewer", "create-proposal.json")
                .whenCommandByUser("user2", "accept-proposal.json")
                .expectError(ContentErrors.unauthorized);
    }

    @Test
    void givenContent_whenAcceptsDifferentProposal_thenError() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json")
                .givenCommandsByUser("Alice", new CreateSighting(new SightingId("2"), new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), true), "create-proposal.json")
                .whenCommandByUser("viewer", "accept-proposal-increment-id.json")
                .expectExceptionalResult(LinkedSightingErrors.notFound)
                .expectError((e) -> e.getMessage().equals(LinkedSightingErrors.notFound.getMessage()));
    }

    @Test
    void givenClaim_whenSameProposal_thenError() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json")
                .givenCommandsByUser("viewer", "../sighting/claim-sighting.json")
                .whenCommand("create-proposal.json")
                .expectExceptionalResult(LinkedSightingErrors.alreadyExists)
                .expectError((e) -> e.getMessage().equals(LinkedSightingErrors.alreadyExists.getMessage()));
    }

    @Nested
    class CreatePublishPropose {
        @BeforeEach
        void setUp() {
            testFixture.registerHandlers(LinkedSightingState.class).givenCommandsByUser("viewer", "../content/create-content.json").givenCommands("../content/publish-content.json")
                    .givenCommandsByUser("Alice", "../sighting/create-sighting.json", "create-proposal.json");
        }



        @Test
        void givenAcceptedProposal_whenQueryProposedSightings_thenEmptyList() {
            testFixture.whenQuery(new GetContent(new ContentId("1")))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .andThen()
                    .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(LinkedSightingStatus.CREATED)))
                    .expectResult(hasSize(1))
                    .andThen()
                    .givenCommands("accept-proposal.json")
                    .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(LinkedSightingStatus.CREATED)))
                    .expectResult(List::isEmpty);
        }

        @Test
        void givenProposal_whenNonExistentProposalRejected_thenError() {
            testFixture.whenCommand(new DeleteLinkedProposal(new LinkedSightingId(new ContentId("1"), new SightingId("2"))))
                    .expectError(LinkedSightingErrors.notFound);
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
            testFixture.whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(LinkedSightingStatus.CREATED)))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .expectResult(hasSize(1));
        }

        @Test
        void givenProposal_whenSameProposal_thenError() {
            testFixture.whenCommand("create-proposal.json")
                    .expectError((e) -> e.getMessage().equals(LinkedSightingErrors.alreadyExists.getMessage()));
        }

        @Test
        void givenAProposal_whenDeleteSighting_thenProposalRemoved() {
            testFixture.whenCommand("../sighting/delete-sighting.json")
                    .expectNoErrors()
                    .expectEvents("../sighting/delete-sighting.json")
                    .expectCommands(DeleteLinkedProposal.class)
                    .andThen()
                    .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(LinkedSightingStatus.CREATED)))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
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
                    .expectCommands(UpdateLastSeenPosition.class)
                    .andThen()
                    .whenQuery(new GetLinkedSightingsByContentIdAndStatuses(new ContentId("1"), List.of(LinkedSightingStatus.ACCEPTED)))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .expectResult(hasSize(1));
        }

        @Test
        void history() {
            testFixture.givenCommands("accept-proposal.json")
                    .whenQuery(new GetSightingHistoryForContent(new ContentId("1")))
                    .expectResult(hasSize(2));
        }
    }
}
