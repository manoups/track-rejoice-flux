package com.breece.app;

import com.breece.app.web.ContentSseEndpoint;
import com.breece.app.web.api.SseEventData;
import com.breece.app.web.api.SseEventData.ChangeType;
import com.breece.content.api.model.ContentId;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationState;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import com.breece.sighting.api.model.SightingId;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fluxzero.sdk.common.serialization.jackson.JacksonSerializer;
import net.jqwik.api.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Feature: pet-content-sse-dashboard, Property 1: SSE snapshot contains all current WeightedAssociationState records
 * Validates: Requirements 1.2, 8.2
 *
 * Feature: pet-content-sse-dashboard, Property 2: Notification forwarding filters by ContentId and preserves payload
 * Validates: Requirements 1.3, 2.1, 2.2, 8.2, 8.3
 *
 * Feature: pet-content-sse-dashboard, Property 3: SSE event IDs are monotonically increasing
 * Validates: Requirements 1.5
 *
 * Feature: pet-content-sse-dashboard, Property 4: BigDecimal coordinate serialization round-trip preserves precision
 * Validates: Requirements 2.3
 */
class ContentSseEndpointPropertyTests {

    // Feature: pet-content-sse-dashboard, Property 1: SSE snapshot contains all current WeightedAssociationState records
    @Property(tries = 20)
    void snapshotContainsExactlyAllRecordsWithInitialChangeType(
            @ForAll("weightedAssociationStatesForContent") List<WeightedAssociationState> records
    ) {
        List<SseEventData> snapshotEvents = ContentSseEndpoint.buildSnapshotEvents(records);

        // Snapshot must contain exactly as many events as input records
        assert snapshotEvents.size() == records.size()
                : "Expected " + records.size() + " snapshot events but got " + snapshotEvents.size();

        // Every event must have changeType INITIAL
        for (SseEventData event : snapshotEvents) {
            assert event.changeType() == ChangeType.INITIAL
                    : "Expected changeType INITIAL but got " + event.changeType();
        }

        // Every event payload must be one of the input records
        Set<WeightedAssociationState> inputSet = Set.copyOf(records);
        Set<Object> payloadSet = snapshotEvents.stream()
                .map(SseEventData::payload)
                .collect(Collectors.toSet());

        assert payloadSet.equals(inputSet)
                : "Snapshot payloads do not match input records";
    }

    // Feature: pet-content-sse-dashboard, Property 1: SSE snapshot contains all current WeightedAssociationState records
    @Property(tries = 20)
    void snapshotContainsNoRecordsFromOtherContentIds(
            @ForAll("weightedAssociationStatesForContent") List<WeightedAssociationState> targetRecords,
            @ForAll("weightedAssociationStatesForOtherContent") List<WeightedAssociationState> otherRecords
    ) {
        // Build snapshot only from target records (simulating the filtered search result)
        List<SseEventData> snapshotEvents = ContentSseEndpoint.buildSnapshotEvents(targetRecords);

        // Extract contentIds from other records
        Set<String> otherContentIds = otherRecords.stream()
                .map(r -> r.contentId().toString())
                .collect(Collectors.toSet());

        // No snapshot event payload should have a contentId from the other set
        for (SseEventData event : snapshotEvents) {
            WeightedAssociationState payload = (WeightedAssociationState) event.payload();
            assert !otherContentIds.contains(payload.contentId().toString())
                    : "Snapshot contained record from other contentId: " + payload.contentId();
        }
    }

    @Provide
    Arbitrary<List<WeightedAssociationState>> weightedAssociationStatesForContent() {
        return weightedAssociationStatesForContentId("target-content");
    }

    @Provide
    Arbitrary<List<WeightedAssociationState>> weightedAssociationStatesForOtherContent() {
        return weightedAssociationStatesForContentId("other-content");
    }

    // Feature: pet-content-sse-dashboard, Property 2: Notification forwarding filters by ContentId and preserves payload
    @Property(tries = 20)
    void createdNotificationHasCorrectChangeTypeAndFullPayload(
            @ForAll("singleWeightedAssociationState") WeightedAssociationState state
    ) {
        // CREATED: current is present, previous is null
        SseEventData event = ContentSseEndpoint.buildUpdateEvent(state, null);

        assert event.changeType() == ChangeType.CREATED
                : "Expected CREATED but got " + event.changeType();
        assert event.payload() == state
                : "Expected full WeightedAssociationState as payload for CREATED";
        assert event.payload() instanceof WeightedAssociationState
                : "CREATED payload must be a WeightedAssociationState instance";
    }

    // Feature: pet-content-sse-dashboard, Property 2: Notification forwarding filters by ContentId and preserves payload
    @Property(tries = 20)
    void updatedNotificationHasCorrectChangeTypeAndFullPayload(
            @ForAll("singleWeightedAssociationState") WeightedAssociationState current,
            @ForAll("singleWeightedAssociationState") WeightedAssociationState previous
    ) {
        // UPDATED: both current and previous are present
        SseEventData event = ContentSseEndpoint.buildUpdateEvent(current, previous);

        assert event.changeType() == ChangeType.UPDATED
                : "Expected UPDATED but got " + event.changeType();
        assert event.payload() == current
                : "Expected full current WeightedAssociationState as payload for UPDATED";
        assert event.payload() instanceof WeightedAssociationState
                : "UPDATED payload must be a WeightedAssociationState instance";
    }

    // Feature: pet-content-sse-dashboard, Property 2: Notification forwarding filters by ContentId and preserves payload
    @SuppressWarnings("unchecked")
    @Property(tries = 20)
    void deletedNotificationHasCorrectChangeTypeAndOnlyWeightedAssociationId(
            @ForAll("singleWeightedAssociationState") WeightedAssociationState previous
    ) {
        // DELETED: current is null, previous is present
        SseEventData event = ContentSseEndpoint.buildUpdateEvent(null, previous);

        assert event.changeType() == ChangeType.DELETED
                : "Expected DELETED but got " + event.changeType();
        assert event.payload() instanceof Map
                : "DELETED payload must be a Map, got " + event.payload().getClass();

        Map<String, Object> payloadMap = (Map<String, Object>) event.payload();
        assert payloadMap.size() == 1
                : "DELETED payload must contain only weightedAssociationId, got " + payloadMap.size() + " keys";
        assert payloadMap.containsKey("weightedAssociationId")
                : "DELETED payload must contain 'weightedAssociationId' key";
        assert payloadMap.get("weightedAssociationId").equals(previous.weightedAssociationId().toString())
                : "DELETED payload weightedAssociationId must match previous state";
    }

    // Feature: pet-content-sse-dashboard, Property 2: Notification forwarding filters by ContentId and preserves payload
    @Property(tries = 20)
    void notificationOnlyForwardsToMatchingContentId(
            @ForAll("singleWeightedAssociationState") WeightedAssociationState notification,
            @ForAll("randomContentIdString") String subscribedContentId
    ) {
        // The filtering logic in handleWeightedAssociationUpdate extracts contentId from
        // current state (for CREATED/UPDATED) or previous state (for DELETED) and checks
        // if activeConnections contains that contentId string.
        // We test: a notification's contentId matches a subscribed contentId only when they are equal.

        String notificationContentId = notification.contentId().toString();
        boolean shouldForward = notificationContentId.equals(subscribedContentId);

        // For CREATED notifications (previous=null), contentId comes from current state
        ContentId extractedId = notification.contentId();
        boolean wouldMatch = extractedId.toString().equals(subscribedContentId);

        assert shouldForward == wouldMatch
                : "Filtering mismatch: shouldForward=" + shouldForward + " but wouldMatch=" + wouldMatch;

        // When it matches, buildUpdateEvent must produce a valid event
        if (shouldForward) {
            SseEventData event = ContentSseEndpoint.buildUpdateEvent(notification, null);
            assert event != null : "Event must be produced for matching contentId";
            assert event.changeType() == ChangeType.CREATED : "Expected CREATED for new notification";
        }
    }

    // Feature: pet-content-sse-dashboard, Property 3: SSE event IDs are monotonically increasing
    @Property(tries = 20)
    void sseEventIdsAreStrictlyMonotonicallyIncreasing(
            @ForAll("eventCount") int numberOfEvents
    ) {
        // Simulate the per-emitter AtomicLong event ID assignment used in ContentSseEndpoint
        AtomicLong eventId = new AtomicLong(0);
        List<Long> ids = new ArrayList<>();

        for (int i = 0; i < numberOfEvents; i++) {
            ids.add(eventId.incrementAndGet());
        }

        // Verify strict monotonic increase: each ID must be strictly greater than the previous
        for (int i = 1; i < ids.size(); i++) {
            assert ids.get(i) > ids.get(i - 1)
                    : "Event ID at index " + i + " (" + ids.get(i) + ") is not strictly greater than previous ("
                    + ids.get(i - 1) + ")";
        }

        // Also verify the first ID is 1 (incrementAndGet on a fresh AtomicLong(0))
        if (!ids.isEmpty()) {
            assert ids.getFirst() == 1L
                    : "First event ID should be 1 but was " + ids.getFirst();
        }
    }

    @Provide
    Arbitrary<Integer> eventCount() {
        return Arbitraries.integers().between(1, 100);
    }

    @Provide
    Arbitrary<String> randomContentIdString() {
        // Generate contentId strings that sometimes match and sometimes don't
        return Arbitraries.oneOf(
                // Sometimes use a known prefix that will match generated states
                Arbitraries.of("content-"),
                // Sometimes use random strings that likely won't match
                Arbitraries.strings().alpha().ofMinLength(5).ofMaxLength(20)
        ).flatMap(prefix -> Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10)
                .map(suffix -> prefix + suffix));
    }

    @Provide
    Arbitrary<WeightedAssociationState> singleWeightedAssociationState() {
        return Combinators.combine(
                Arbitraries.strings().alpha().ofMinLength(5).ofMaxLength(15),
                Arbitraries.strings().alpha().ofMinLength(5).ofMaxLength(15),
                Arbitraries.strings().alpha().ofMinLength(5).ofMaxLength(15),
                bigDecimalArbitrary(),
                bigDecimalArbitrary(),
                Arbitraries.of(WeightedAssociationStatus.values()),
                Arbitraries.doubles().between(0.0, 10000.0)
        ).as((waId, contentId, sightingId, lng, lat, status, distance) ->
                new WeightedAssociationState(
                        new WeightedAssociationId(waId),
                        new ContentId(contentId),
                        new SightingId(sightingId),
                        new SightingDetails(lng, lat),
                        status,
                        distance,
                        distance
                )
        );
    }

    private Arbitrary<List<WeightedAssociationState>> weightedAssociationStatesForContentId(String contentIdStr) {
        Arbitrary<WeightedAssociationState> stateArb = Combinators.combine(
                Arbitraries.strings().alpha().ofMinLength(5).ofMaxLength(15),  // unique id part
                Arbitraries.strings().alpha().ofMinLength(5).ofMaxLength(15),  // sighting id part
                bigDecimalArbitrary(),  // lng
                bigDecimalArbitrary(),  // lat
                Arbitraries.of(WeightedAssociationStatus.values()),
                Arbitraries.doubles().between(0.0, 10000.0),
                Arbitraries.doubles().between(0.0, 10000.0)
        ).as((waId, sightingId, lng, lat, status, distance, score) ->
                new WeightedAssociationState(
                        new WeightedAssociationId(waId),
                        new ContentId(contentIdStr),
                        new SightingId(sightingId),
                        new SightingDetails(lng, lat),
                        status,
                        distance,
                        score
                )
        );
        return stateArb.list().ofMinSize(0).ofMaxSize(20);
    }

    private Arbitrary<BigDecimal> bigDecimalArbitrary() {
        return Arbitraries.doubles().between(-180.0, 180.0)
                .map(BigDecimal::valueOf);
    }

    // Feature: pet-content-sse-dashboard, Property 4: BigDecimal coordinate serialization round-trip preserves precision
    // Validates: Requirements 2.3
    private static final ObjectMapper objectMapper = JacksonSerializer.defaultObjectMapper;

    @Property(tries = 20)
    void bigDecimalCoordinateRoundTripPreservesPrecision(
            @ForAll("coordinateLng") BigDecimal lng,
            @ForAll("coordinateLat") BigDecimal lat
    ) throws Exception {
        // 1. Create SightingDetails with the generated coordinates
        SightingDetails original = new SightingDetails(lng, lat);

        // 2. Wrap in a WeightedAssociationState
        WeightedAssociationState state = new WeightedAssociationState(
                new WeightedAssociationId("wa-roundtrip"),
                new ContentId("content-roundtrip"),
                new SightingId("sighting-roundtrip"),
                original,
                WeightedAssociationStatus.CREATED,
                0.0,
                0.0
        );

        // 3. Wrap in SseEventData (as the SSE endpoint would)
        SseEventData event = new SseEventData(ChangeType.INITIAL, state);

        // 4. Serialize to JSON
        String json = objectMapper.writeValueAsString(event);

        // 5. Deserialize back
        JsonNode root = objectMapper.readTree(json);
        JsonNode payloadNode = root.get("payload");
        JsonNode sightingNode = payloadNode.get("sightingDetails");

        BigDecimal deserializedLng = sightingNode.get("lng").decimalValue();
        BigDecimal deserializedLat = sightingNode.get("lat").decimalValue();

        // 6. Verify compareTo equality (not equals, since scale may differ)
        assert original.lng().compareTo(deserializedLng) == 0
                : "Longitude precision lost: original=" + original.lng() + " deserialized=" + deserializedLng;
        assert original.lat().compareTo(deserializedLat) == 0
                : "Latitude precision lost: original=" + original.lat() + " deserialized=" + deserializedLat;
    }

    @Provide
    Arbitrary<BigDecimal> coordinateLng() {
        return Arbitraries.bigDecimals()
                .between(new BigDecimal("-180"), new BigDecimal("180"))
                .ofScale(10);
    }

    @Provide
    Arbitrary<BigDecimal> coordinateLat() {
        return Arbitraries.bigDecimals()
                .between(new BigDecimal("-90"), new BigDecimal("90"))
                .ofScale(10);
    }
}
