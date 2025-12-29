package com.breece.trackrejoice;

import com.breece.trackrejoice.content.ContentErrors;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

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
    void rejectProposal() {
        testFixture.givenCommands("sighting/create-sighting.json", "content/create-content.json", "proposal/create-proposal.json")
                .whenCommand("proposal/reject-proposal.json")
                .expectNoErrors()
                .expectNoEvents()
        ;
    }
}
