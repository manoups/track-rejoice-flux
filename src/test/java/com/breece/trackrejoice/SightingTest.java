package com.breece.trackrejoice;

import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.SightingState;
import com.breece.trackrejoice.content.command.ClaimSighting;
import com.breece.trackrejoice.sighting.api.GetOpenSightings;
import com.breece.trackrejoice.sighting.SightingErrors;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import com.breece.trackrejoice.user.api.UserId;
import com.breece.trackrejoice.user.api.model.UserProfile;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;

public class SightingTest {
    final TestFixture testFixture = TestFixture.create(SightingState.class).givenCommands("user/create-user.json").givenCommands("user/create-another-user.json");
    final UserProfile viewer = new UserProfile(new UserId("viewer"), null, null);

    @Test
    void createSighting() {
        testFixture.whenCommand("sighting/create-sighting.json")
                .expectEvents("sighting/create-sighting.json");
    }

    @Test
    void claimSightingWithCorrectContentOwner() {
        testFixture
                .givenCommandsByUser(viewer,
                        "content/create-content.json", "sighting/create-sighting.json")
                .whenCommandByUser(viewer, "sighting/claim-sighting.json")
                .expectNoErrors()
                .expectEvents("sighting/claim-sighting.json");
    }

    @Test
    void claimSightingForContentOfOtherUser() {
        testFixture
                .givenCommandsByUser(viewer,
                        "content/create-content.json", "sighting/create-sighting.json")
                .whenCommandByUser(new UserProfile(new UserId("user2"), null, null), "sighting/claim-sighting.json")
                .expectExceptionalResult(ContentErrors.notFound);
    }

    @Test
    void claimAlreadyClaimedSighting() {
        UserProfile user2 = new UserProfile(new UserId("user2"), null, null);
        testFixture
                .givenCommandsByUser(viewer,
                        "content/create-content.json", "sighting/create-sighting.json", "sighting/claim-sighting.json")
                .givenCommandsByUser(user2, "content/create-content-keys.json")
                .whenCommandByUser(user2, new ClaimSighting(new ContentId("2"), new SightingId("1")))
                .expectExceptionalResult(SightingErrors.alreadyClaimed);
    }

    @Test
    void givenSighting_whenGetSightings_thenOneResults() {
        testFixture
                .givenCommands("sighting/create-sighting.json")
                .whenQuery(new GetOpenSightings())
                .expectResult(hasSize(1));
    }

    @Test
    void givenSightingClaimed_whenGetSightings_thenNoResults() {
        testFixture
                .givenCommandsByUser(viewer,
                        "content/create-content.json", "sighting/create-sighting.json", "sighting/claim-sighting.json")
                .whenQuery(new GetOpenSightings())
                .expectResult(List::isEmpty);
    }
}
