package com.breece.trackrejoice;

import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.command.ClaimSighting;
import com.breece.trackrejoice.content.command.UpdateContent;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.query.GetContent;
import com.breece.trackrejoice.geo.GeometryUtil;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class ProposalTest {
    final TestFixture testFixture = TestFixture.create();

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
                .mapResult(content -> content.details().getLastConfirmedSighting())
                .expectResult(Objects::nonNull)
                .expectResult(details -> GeometryUtil.parseLocation(details.lat(), details.lng()).within(GeometryUtil.parseLocation(0.0, 0.0)));
    }

    @Test
    void givenProposedSighting_whenQueryContent_thenCoordsDoNotChange() {
        testFixture.givenCommands("content/create-content.json", "sighting/create-sighting.json", "proposal/create-proposal.json")
                .whenQuery(new GetContent(new ContentId("1")))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .mapResult(content -> content.details().getLastConfirmedSighting())
                .expectResult(Objects::nonNull)
                .expectResult(details -> GeometryUtil.parseLocation(details.lat(), details.lng()).within(GeometryUtil.parseLocation(0.0, 0.0)));
    }

    @Test
    void confirmProposalCommands() {
        testFixture.givenCommands("content/create-content.json", "sighting/create-sighting.json", "proposal/create-proposal.json")
                .whenCommand("proposal/accept-proposal.json")
                .expectNoErrors()
                .expectCommands(ClaimSighting.class, UpdateContent.class);
    }

    @Test
    void confirmProposal() {
        testFixture.givenCommands("content/create-content.json", "sighting/create-sighting.json", "proposal/create-proposal.json", "proposal/accept-proposal.json")
                .whenQuery(new GetContent(new ContentId("1")))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .mapResult(content -> content.details().getLastConfirmedSighting())
                .expectResult(Objects::nonNull)
                .expectResult(details -> GeometryUtil.parseLocation(details.lat(), details.lng()).within(GeometryUtil.parseLocation(123.456, 78.901)));
    }

    @Test
    void rejectProposal() {
        testFixture.givenCommands("sighting/create-sighting.json", "content/create-content.json", "proposal/create-proposal.json")
                .whenCommand("proposal/reject-proposal.json")
                .expectNoErrors()
                .expectNoEvents()
        ;
    }
}
