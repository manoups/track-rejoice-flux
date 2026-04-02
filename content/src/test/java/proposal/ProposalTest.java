package proposal;

import com.breece.content.ContentErrors;
import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.GenderEnum;
import com.breece.content.api.model.Pet;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.CreateContent;
import com.breece.content.command.api.PublishContent;
import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.content.query.api.GetContent;
import com.breece.content.query.api.GetContents;
import com.breece.content.query.api.GetSightingHistoryForContent;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.common.SightingEnum;
import com.breece.coreapi.facets.FacetFilter;
import com.breece.coreapi.facets.GetFacetStatsResult;
import com.breece.coreapi.facets.GetFacets;
import com.breece.coreapi.facets.Pagination;
import com.breece.coreapi.util.GeometryUtil;
import com.breece.proposal.command.api.*;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationState;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.CreateSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.test.TestFixture;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import util.TestUtilities;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;

@Slf4j
public class ProposalTest extends TestUtilities {
    final TestFixture testFixture = TestFixture.create(WeightedAssociationState.class, ContentState.class).givenCommands(createUserFromProfile(viewer), createUserFromProfile(user2), createUserFromProfile(Alice));

    @Test
    void givenNoContent_whenProposalCreated_thenError() {
        testFixture.givenCommands("../sighting/create-sighting.json")
                .whenCommand("create-proposal.json")
                .expectError(ContentErrors.notFound);
    }

    @Test
    void createProposal() {
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json", "../sighting/create-sighting.json")
                .givenCommands("../content/publish-content.json", "create-weighted-association.json")
                .whenCommand("create-proposal.json")
                .expectNoErrors()
                .expectEvents("create-proposal.json");
    }

   /* @Test
    void givenIncorrectLinkedSightingId_whenCreateProposal_thenError() {
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json", "../sighting/create-sighting.json")
                .givenCommands("../content/publish-content.json")
                .whenCommandByUser("viewer", new CreateWeightedAssociation(new ContentId("1"), new WeightedAssociationId(new ContentId("3"), new SightingId("2")), new SightingId("1"),
                        new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO)))
                .expectError(UnauthorizedException.class);
    }*/

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
                .givenCommands("../content/publish-content.json", "create-weighted-association.json")
                .whenCommandByUser("Alice", "create-proposal.json")
                .expectNoErrors()
                .expectCommands()
                .expectEvents("create-proposal.json");
    }

    @Test
    void ProposalShouldBeLinkedToContentIfUserMatch() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json", "create-weighted-association.json")
                .whenCommandByUser("viewer", "create-proposal.json")
                .expectNoErrors()
                .expectCommands(UpdateLastSeenPosition.class)
                .expectEvents("create-proposal.json", AcceptProposal.class, UpdateLastSeenPosition.class);
    }

    @Test
    void givenRejectProposal_whenQueryProposedSightings_thenEmptyList() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json", "create-weighted-association.json")
                .givenCommandsByUser("Alice", "create-proposal.json")
                .givenCommandsByUser("viewer", "reject-proposal.json")
                .whenQuery(new GetWeightedAssociationsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.CREATED)))
                .expectNoErrors()
                .expectResult(Objects::nonNull)
                .expectResult(List::isEmpty);
    }

    @Test
    void givenContentOfUserA_whenUserBCreatesProposal_thenNoError() {
        testFixture.givenCommandsByUser("viewer", "../content/create-content.json")
                .givenCommandsByUser("user2", "../sighting/create-sighting.json")
                .givenCommands("../content/publish-content.json", "create-weighted-association.json")
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

    /*@Test
    void whenPublishContent_then() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .whenCommand("../content/publish-content.json")
                .expectOnlyCommands(CreateWeightedAssociation.class);
    }*/

    @Test
    void givenContentOfUserA_whenUserBRemovesProposal_thenError() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json", "create-weighted-association.json")
                .givenCommandsByUser("viewer", "create-proposal.json")
                .whenCommandByUser("user2", "reject-proposal.json")
                .expectError(ContentErrors.unauthorized)
                .expectExceptionalResult((e) -> e.getMessage().equals(ContentErrors.unauthorized.getMessage()));
    }

    @Test
    void givenContentOfUserA_whenUserBAcceptsProposal_thenError() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json", "create-weighted-association.json")
                .givenCommandsByUser("viewer", "create-proposal.json")
                .whenCommandByUser("user2", "accept-proposal.json")
                .expectError(ContentErrors.unauthorized)
                .expectExceptionalResult((e) -> e.getMessage().equals(ContentErrors.unauthorized.getMessage()));
    }

    @Test
    void givenContent_whenAcceptsDifferentProposal_thenError() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json", "create-weighted-association.json")
                .givenCommandsByUser("Alice", new CreateSighting(new SightingId("2"), new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), true, SightingEnum.CAT), "create-proposal.json")
                .whenCommandByUser("viewer", "accept-proposal-increment-id.json")
                .expectExceptionalResult(WeightedAssociationErrors.notFound)
                .expectError((e) -> e.getMessage().equals(WeightedAssociationErrors.notFound.getMessage()));
    }

    @Test
    void givenClaim_whenSameProposal_thenError() {
        testFixture.givenCommandsByUser("viewer", "../sighting/create-sighting.json", "../content/create-content.json")
                .givenCommands("../content/publish-content.json", "create-weighted-association.json")
                .givenCommandsByUser("viewer", "../sighting/claim-sighting.json")
                .whenCommand("create-proposal.json")
                .expectNoErrors();
    }

    @Test
    void givenMultipleContents_whenRelevantSighting_thenMultipleWeightedAssociations() {
        int SIZE = 15;
        CreateContent[] contents = new CreateContent[SIZE];
        PublishContent[] publishContents = new PublishContent[SIZE];
        CreateWeightedAssociation[] creteWeighedAssociations = new CreateWeightedAssociation[SIZE];
        for (int i = 0; i < SIZE; ++i) {
            ContentId contentId = new ContentId();
            contents[i] = new CreateContent(contentId, new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), new Pet("Maya", "Cocker Spaniel", GenderEnum.FEMALE, SightingEnum.CAT));
            publishContents[i] = new PublishContent(contentId, Duration.ofDays(90));
            creteWeighedAssociations[i] = new CreateWeightedAssociation(contentId, new WeightedAssociationId(), new SightingId("2"), new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO));
        }
        testFixture.givenCommandsByUser("viewer", contents)
                .givenCommands(publishContents)
                .whenNothingHappens()
                .expectTrue(_ -> Fluxzero.search(WeightedAssociationState.class).count() == 0)
                .andThen()
                .whenCommandByUser("Alice", "../sighting/create-sighting.json")
                .andThen()
                .givenCommands(creteWeighedAssociations)
                .whenNothingHappens()
                .expectTrue(_ -> Fluxzero.search(WeightedAssociationState.class).count() == SIZE);

    }

    @Nested
    class AcceptProposalTests {
        CreateContent createContent;
        PublishContent publishContent;
        CreateSighting[] sightings;
        CreateWeightedAssociation[] createWeighedAssociations;
        final int SIZE = 7;

        @BeforeEach
        void setUp() {
            sightings = new CreateSighting[SIZE];
            createWeighedAssociations = new CreateWeightedAssociation[SIZE];
            ContentId contentId = new ContentId();
            for (int i = 0; i < SIZE; ++i) {
                sightings[i] = new CreateSighting(new SightingId(), new SightingDetails(BigDecimal.valueOf(i), BigDecimal.valueOf(i * 0.1)), false, SightingEnum.DOG);
                createWeighedAssociations[i] = new CreateWeightedAssociation(contentId, new WeightedAssociationId(), sightings[i].sightingId(), sightings[i].sightingDetails());
            }
            publishContent = new PublishContent(contentId, Duration.ofDays(90));
            createContent = new CreateContent(contentId, new SightingDetails(new BigDecimal("-1"), new BigDecimal("-1")), new Pet("Maya", "Cocker Spaniel", GenderEnum.FEMALE, SightingEnum.DOG));
        }

        @Test
        void givenProposal_whenQueryContent_thenDistanceOfOtherProposedSightingsChange() {
            testFixture.givenCommands(sightings, createContent, publishContent, createWeighedAssociations)
                    .whenQuery(new GetWeightedAssociationStates(List.of(new FacetFilter("status", List.of(WeightedAssociationStatus.CREATED))), "", new Pagination(0, 10)))
                    .expectNoErrors()
                    .expectResult(hasSize(SIZE));
        }

        @Test
        void givenContentThenSightingCreated_whenQuerySightings_thenDistanceOfOtherProposedSightingsChange() {
            testFixture.givenCommands(createContent, publishContent, sightings, createWeighedAssociations)
                    .whenQuery(new GetWeightedAssociationStates(List.of(new FacetFilter("status", List.of(WeightedAssociationStatus.CREATED))), "", new Pagination(0, 10)))
                    .expectNoErrors()
                    .expectResult(hasSize(SIZE));
        }

        @Test
        void givenProposal_whenAcceptContent_thenStateOfWeightedAssociationChange() {
            testFixture.givenCommands(createContent, publishContent, sightings, createWeighedAssociations)
                    .givenCommands(new ClaimSighting(createContent.contentId(), createWeighedAssociations[0].weightedAssociationId()))
                    .whenQuery(new GetWeightedAssociationStates(List.of(new FacetFilter("status", List.of(WeightedAssociationStatus.CREATED))), "", new Pagination(0, 10)))
                    .expectNoErrors()
                    .expectResult(tr -> tr.size() == SIZE - 1)
                    .andThen()
                    .whenQuery(new GetWeightedAssociationStates(List.of(new FacetFilter("status", List.of(WeightedAssociationStatus.ACCEPTED))), "", new Pagination(0, 10)))
                    .expectNoErrors()
                    .expectResult(hasSize(1));
        }

        @Test
        void givenProposal_whenAcceptContent_thenLastSeenLocationIsUpdated() {
            testFixture.givenCommands(createContent, publishContent, sightings, new ClaimSighting(createContent.contentId(), new WeightedAssociationId()))
                    .whenQuery(new GetContent(createContent.contentId()))
                    .expectNoErrors()
                    .mapResult(Content::lastConfirmedSighting)
                    .expectResult(details -> GeometryUtil.parseLocation(details.lat(), details.lng()).within(GeometryUtil.parseLocation(sightings[0].sightingDetails().lat(), sightings[0].sightingDetails().lng())));
        }

        @Test
        void givenProposal_whenAcceptContent_thenDistanceOfOtherProposedSightingsChange() {
            SightingId targetSightingId = sightings[1].sightingId();
            testFixture.givenCommands(createContent, publishContent, sightings)
                    .whenQuery(new GetWeightedAssociationsBySightingIdAndStatuses(targetSightingId, List.of(WeightedAssociationStatus.CREATED)))
                    .expectThat(fz -> {
                        WeightedAssociationState weightedAssociationState = fz.queryGateway().sendAndWait(new GetWeightedAssociationsBySightingIdAndStatuses(targetSightingId, List.of(WeightedAssociationStatus.CREATED))).getFirst();
                        Content content = fz.queryGateway().sendAndWait(new GetContent(createContent.contentId()));
                        Assertions.assertTrue(Math.abs(weightedAssociationState.distance() - distance(content.lastConfirmedSighting().lat(), content.lastConfirmedSighting().lng(), sightings[1].sightingDetails().lat(), sightings[1].sightingDetails().lng())) < 0.00001);
                    })
                    .andThen()
                    .whenCommand(new ClaimSighting(createContent.contentId(), new WeightedAssociationId()))
                    .expectThat(fz -> {
                        WeightedAssociationState weightedAssociationState = fz.queryGateway().sendAndWait(new GetWeightedAssociationsBySightingIdAndStatuses(targetSightingId, List.of(WeightedAssociationStatus.CREATED))).getFirst();
                        Content content = fz.queryGateway().sendAndWait(new GetContent(createContent.contentId()));
                        Assertions.assertTrue(Math.abs(weightedAssociationState.distance() - distance(content.lastConfirmedSighting().lat(), content.lastConfirmedSighting().lng(), sightings[1].sightingDetails().lat(), sightings[1].sightingDetails().lng())) < 0.00001);
                    });
        }

        private double distance(BigDecimal sourceLat, BigDecimal sourceLng, BigDecimal targetLat, BigDecimal targetLng) {
            return GeometryUtil.parseLocation(sourceLat, sourceLng).distance(GeometryUtil.parseLocation(targetLat, targetLng));
        }
    }

    @Nested
    class FacetStatsTest {
        CreateContent[] contents;
        PublishContent[] publishContents;
        CreateSighting[] sightings;
        List<SightingEnum> targetSightingTypes = List.of(SightingEnum.CAT, SightingEnum.DOG);

        @BeforeEach
        void setUp() {
            int SIZE = 7;
            contents = new CreateContent[SIZE];
            publishContents = new PublishContent[SIZE];
            sightings = new CreateSighting[SIZE];
            for (int i = 0; i < SIZE; ++i) {
                ContentId contentId = new ContentId();
                contents[i] = new CreateContent(contentId, new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), new Pet("Maya", "Cocker Spaniel", GenderEnum.FEMALE, targetSightingTypes.get(i % targetSightingTypes.size())));
                publishContents[i] = new PublishContent(contentId, Duration.ofDays(90));
                sightings[i] = new CreateSighting(new SightingId(), new SightingDetails(BigDecimal.ZERO, BigDecimal.ZERO), true, targetSightingTypes.get(i % targetSightingTypes.size()));
            }
        }

        @Test
        void checkFacetStats() {
            testFixture.givenCommands(contents, publishContents, sightings)
                    .whenQuery(new GetFacets(new GetContents(List.of(), "", new Pagination(0, 10))))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .mapResult(GetFacetStatsResult::getStats)
                    .mapResult(l -> l.get("details/subtype"))
                    .expectResult(hasSize(targetSightingTypes.size()));
        }

        @Test
        void checkFacetStatsWithFilter() {
            testFixture.givenCommands(contents, publishContents, sightings)
                    .whenQuery(new GetFacets(new GetContents(List.of(new FacetFilter("details/subtype", SightingEnum.CAT)), "", new Pagination(0, 10))))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .mapResult(GetFacetStatsResult::getStats)
                    .mapResult(l -> l.get("details/subtype"))
                    .expectResult(hasSize(1));
        }
    }

    @Nested
    class CreatePublishPropose {
        @BeforeEach
        void setUp() {
            testFixture.givenCommandsByUser("viewer", "../content/create-content.json").givenCommands("../content/publish-content.json")
                    .givenCommandsByUser("Alice", "../sighting/create-sighting.json", "create-proposal.json");
        }

        @Test
        void givenAcceptedProposal_whenQueryProposedSightings_thenEmptyList() {
            testFixture.whenQuery(new GetContent(new ContentId("1")))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .andThen()
                    .whenQuery(new GetWeightedAssociationsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.LINKED)))
                    .expectResult(hasSize(1))
                    .andThen()
                    .givenCommands("accept-proposal.json")
                    .whenQuery(new GetWeightedAssociationsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.LINKED)))
                    .expectResult(List::isEmpty);
        }

        @Test
        void givenProposal_whenNonExistentProposalRejected_thenError() {
            ContentId contentId = new ContentId("1");
            testFixture.whenCommand(new DeleteWeightedAssociation(contentId, new WeightedAssociationId()))
                    .expectError(WeightedAssociationErrors.notFound);
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
            testFixture.whenQuery(new GetWeightedAssociationsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.LINKED)))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .expectResult(hasSize(1));
        }

        @Test
        void givenProposal_whenSameProposal_thenSilentlyAccept() {
            testFixture.whenCommandByUser("Alice", "create-proposal.json")
                    .expectNoErrors()
                    .expectNoCommands();
        }

        @Test
        void givenProposal_whenAcceptedProposal_thenCommands() {
            testFixture.whenCommandByUser("viewer", "accept-proposal.json")
                    .expectNoErrors()
                    .expectOnlyEvents(AcceptProposal.class, UpdateLastSeenPosition.class)
                    .expectOnlyCommands(UpdateLastSeenPosition.class);
        }

        @Test
        void givenAcceptedProposal_whenSameProposal_thenNoCommands() {
            testFixture.givenCommandsByUser("viewer", "accept-proposal.json")
                    .whenCommandByUser("Alice", "create-proposal.json")
                    .expectNoErrors()
                    .expectNoCommands();
        }

        @Test
        void givenAProposal_whenDeleteSighting_thenProposalRemoved() {
            testFixture.whenCommand("../sighting/delete-sighting.json")
                    .expectNoErrors()
                    .expectEvents("../sighting/delete-sighting.json")
                    .expectCommands(DeleteWeightedAssociation.class)
                    .andThen()
                    .whenQuery(new GetWeightedAssociationsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.CREATED)))
                    .expectNoErrors()
                    .expectResult(Objects::nonNull)
                    .expectResult(List::isEmpty);
        }

        @Test
//        @Disabled
        void givenAnAcceptedProposal_whenDeleteSighting_thenProposalAlsoDeleted() {
            testFixture.givenCommands("accept-proposal.json")
                    .whenCommand("../sighting/delete-sighting.json")
                    .expectNoErrors()
                    .expectEvents("../sighting/delete-sighting.json")
                    .expectOnlyCommands(DeleteWeightedAssociation.class)
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
                    .whenQuery(new GetWeightedAssociationsByContentIdAndStatuses(new ContentId("1"), List.of(WeightedAssociationStatus.ACCEPTED)))
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
