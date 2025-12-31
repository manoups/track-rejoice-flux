package com.breece.trackrejoice;

import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.ProposedSightingHandler;
import com.breece.trackrejoice.content.command.AcceptProposal;
import com.breece.trackrejoice.content.command.ProposedSightingErrors;
import com.breece.trackrejoice.content.command.RemoveMemberProposal;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ProposedSightingId;
import com.breece.trackrejoice.content.query.GetContent;
import com.breece.trackrejoice.geo.GeometryUtil;
import com.breece.trackrejoice.sighting.SightingErrors;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

public class ProposalTest extends TestUtilities{
    final TestFixture testFixture = TestFixture.create(ProposedSightingHandler.class);

    @Test
    void givenNoContent_whenProposalCreated_thenError() {
        testFixture.givenCommands("sighting/create-sighting.json")
                .whenCommand("proposal/create-proposal.json")
                .expectError(ContentErrors.notFound);
    }

    @Test
    void createProposal() {
        testFixture.givenCommands("content/create-content.json", "sighting/create-sighting.json")
                .whenCommand("proposal/create-proposal.json")
                .expectNoErrors()
                .expectEvents("proposal/create-proposal.json");
    }

    @Test
    void confirmCoords() {
        testFixture.givenCommands("content/create-content.json")
                .whenQuery(new GetContent(new ContentId("1")))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .mapResult(Content::lastConfirmedSighting)
                .expectResult(Objects::nonNull)
                .expectResult(details -> GeometryUtil.parseLocation(details.lat(), details.lng()).within(GeometryUtil.parseLocation(0.0, 0.0)));
    }

    @Test
    void givenProposedSighting_whenQueryContent_thenCoordsDoNotChange() {
        testFixture.givenCommands("content/create-content.json", "sighting/create-sighting.json", "proposal/create-proposal.json")
                .whenQuery(new GetContent(new ContentId("1")))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .mapResult(Content::lastConfirmedSighting)
                .expectResult(Objects::nonNull)
                .expectResult(details -> GeometryUtil.parseLocation(details.lat(), details.lng()).within(GeometryUtil.parseLocation(0.0, 0.0)));
    }

    @Test
    void confirmProposalCommands() {
        testFixture.givenCommands("content/create-content.json", "sighting/create-sighting.json", "proposal/create-proposal.json")
                .whenCommand("proposal/accept-proposal.json")
                .expectNoErrors()
                .expectEvents("proposal/accept-proposal.json");
    }

    @Test
    void confirmProposal() {
        testFixture.givenCommands("content/create-content.json", "sighting/create-sighting.json", "proposal/create-proposal.json", "proposal/accept-proposal.json")
                .whenQuery(new GetContent(new ContentId("1")))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .mapResult(Content::lastConfirmedSighting)
                .expectResult(Objects::nonNull)
                .expectResult(details -> GeometryUtil.parseLocation(details.lat(), details.lng()).within(GeometryUtil.parseLocation(123.456, 78.901)));
    }

    @Test
    void givenSighting_whenCreateProposal_thenContentShouldContainTheProposal() {
        testFixture.givenCommands("content/create-content.json", "sighting/create-sighting.json", "proposal/create-proposal.json")
                .whenQuery(new GetContent(new ContentId("1")))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .mapResult(Content::proposedSightings)
                .expectResult(hasSize(1));
    }

    @Test
    void rejectProposal() {
        testFixture.givenCommands("sighting/create-sighting.json", "content/create-content.json", "proposal/create-proposal.json")
                .whenCommand("proposal/reject-proposal.json")
                .expectNoErrors()
                .expectEvents("proposal/reject-proposal.json");
    }

    @Test
    void givenRejectProposal_whenQueryProposedSightings_thenEmptyList() {
        testFixture.givenCommands("sighting/create-sighting.json", "content/create-content.json", "proposal/create-proposal.json", "proposal/reject-proposal.json")
                .whenQuery(new GetContent(new ContentId("1")))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .mapResult(Content::proposedSightings)
                .expectResult(List::isEmpty);
    }

    @Test
    void givenAcceptedProposal_whenQueryProposedSightings_thenEmptyList() {
        testFixture.givenCommands("sighting/create-sighting.json", "content/create-content.json", "proposal/create-proposal.json")
                .whenQuery(new GetContent(new ContentId("1")))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .mapResult(Content::proposedSightings)
                .expectResult(hasSize(1))
                .andThen()
                .givenCommands("proposal/accept-proposal.json")
                .whenQuery(new GetContent(new ContentId("1")))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .mapResult(Content::proposedSightings)
                .expectResult(List::isEmpty);
    }

    @Test
    void givenContentOfUserA_whenUserBCreatesProposal_thenNoError() {
        testFixture.givenCommandsByUser(viewer,  "content/create-content.json")
                .givenCommandsByUser(user2, "sighting/create-sighting.json")
                .whenCommandByUser(user2,"proposal/create-proposal.json")
                .expectNoErrors()
                .expectEvents("proposal/create-proposal.json");
    }

    @Test
    void givenContentOfUserASightingOfUserB_whenUserAliceCreatesProposal_thenError() {
        testFixture.givenCommandsByUser(viewer,  "content/create-content.json")
                .givenCommandsByUser(user2, "sighting/create-sighting.json")
                .whenCommandByUser(Alice,"proposal/create-proposal.json")
                .expectError(SightingErrors.notOwner);
    }

    @Test
    void givenContentOfUserA_whenUserBRemovesProposal_thenError() {
        testFixture.givenCommandsByUser(viewer, "sighting/create-sighting.json", "content/create-content.json", "proposal/create-proposal.json")
                .whenCommandByUser(user2, "proposal/reject-proposal.json")
                .expectError(IllegalCommandException.class);
    }

    @Test
    void givenContentOfUserA_whenUserBAcceptsProposal_thenError() {
        testFixture.givenCommandsByUser(viewer, "sighting/create-sighting.json", "content/create-content.json", "proposal/create-proposal.json")
                .whenCommandByUser(user2, "proposal/accept-proposal.json")
                .expectError(ContentErrors.unauthorized);
    }

    @Test
    void givenProposal_whenNonExistentProposalRejected_thenError() {
        testFixture.givenCommands("sighting/create-sighting.json", "content/create-content.json", "proposal/create-proposal.json")
                .whenCommand(new RemoveMemberProposal(new ProposedSightingId("2")))
                .expectError(ProposedSightingErrors.notFound);
    }
}
