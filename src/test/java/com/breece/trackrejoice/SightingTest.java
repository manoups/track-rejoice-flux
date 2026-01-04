package com.breece.trackrejoice;

import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.ContentHandler;
import com.breece.trackrejoice.content.command.ClaimSighting;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.LinkSightingBackToContent;
import com.breece.trackrejoice.sighting.SightingErrors;
import com.breece.trackrejoice.sighting.api.GetSighting;
import com.breece.trackrejoice.sighting.api.GetSightings;
import com.breece.trackrejoice.sighting.api.SightingHandler;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import com.breece.trackrejoice.user.api.UserId;
import com.breece.trackrejoice.user.api.model.UserProfile;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

public class SightingTest extends TestUtilities{
    final TestFixture testFixture = TestFixture.create(ContentHandler.class, SightingHandler.class ).givenCommands("user/create-user.json").givenCommands("user/create-another-user.json");


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
                .expectEvents("sighting/claim-sighting.json")
                .expectCommands(LinkSightingBackToContent.class)
                .andThen()
                .whenQuery(new GetSighting(new SightingId("1")))
                .expectResult(Objects::nonNull)
                .mapResult(Sighting::linkedContents)
                .expectResult(hasSize(1));
    }

    @Test
    void claimSightingForContentOfOtherUser() {
        testFixture
                .givenCommandsByUser(viewer,
                        "content/create-content.json", "sighting/create-sighting.json")
                .whenCommandByUser(new UserProfile(new UserId("user2"), null, null), "sighting/claim-sighting.json")
                .expectExceptionalResult(ContentErrors.notFound);
    }

    @Disabled
    @Test
    void claimAlreadyClaimedSighting() {
        testFixture
                .givenCommandsByUser(viewer,
                        "content/create-content.json", "sighting/create-sighting.json", "sighting/claim-sighting.json")
                .givenCommandsByUser(user2, "content/create-content-keys.json")
                .whenCommandByUser(user2, new ClaimSighting(new ContentId("2"), new SightingId("1"), new SightingDetails(
                        new BigDecimal("78.901"), new BigDecimal("123.456")
        )))
                .expectExceptionalResult(SightingErrors.notLinkedToContent);
    }

    @Test
    void givenSighting_whenGetSightings_thenOneResults() {
        testFixture
                .givenCommands("sighting/create-sighting.json")
                .whenQuery(new GetSightings())
                .expectResult(hasSize(1));
    }

    @Disabled
    @Test
    void givenSightingClaimed_whenGetSightings_thenNoResults() {
        testFixture
                .givenCommandsByUser(viewer,
                        "content/create-content.json", "sighting/create-sighting.json", "sighting/claim-sighting.json")
                .whenQuery(new GetSightings())
                .expectResult(List::isEmpty);
    }

    @Test
    void givenSighting_whenClaimSightingWithInvertedCoords_thenError() {
        testFixture
                .givenCommands("content/create-content.json", "sighting/create-sighting.json")
                .whenCommand(new ClaimSighting(new ContentId("1"), new SightingId("1"), new SightingDetails(
                        new BigDecimal("123.456"), new BigDecimal("78.901")
                )))
                .expectExceptionalResult(SightingErrors.sightingMismatch);
    }

    @Test
    void givenSighting_whenClaimSightingWithExtraDecimals_thenNoError() {
        testFixture
                .givenCommands("content/create-content.json", "sighting/create-sighting.json")
                .whenCommand(new ClaimSighting(new ContentId("1"), new SightingId("1"), new SightingDetails(
                        new BigDecimal("78.901000"), new BigDecimal("123.456000")
                )))
                .expectNoErrors();
    }
}
