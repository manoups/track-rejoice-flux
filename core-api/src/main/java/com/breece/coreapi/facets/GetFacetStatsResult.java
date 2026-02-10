package com.breece.coreapi.facets;

import io.fluxzero.common.api.JsonType;
import io.fluxzero.common.api.search.FacetStats;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
public class GetFacetStatsResult implements JsonType {

    public GetFacetStatsResult(List<FacetStats> stats) {
        this.stats = stats.stream()
                .collect(Collectors.groupingBy(FacetStats::getName, Collectors.mapping(ValueCountPair::new, Collectors.toList())));
    }

    Map<String, List<ValueCountPair>> stats;

    /**
     * Timestamp indicating when this result was generated (milliseconds since epoch).
     */
    long timestamp = System.currentTimeMillis();

    @Override
    public Metric toMetric() {
        return new Metric(stats.size(), timestamp);
    }

    /**
     * Lightweight summary of the facet statistics result, used for internal metric tracking.
     * <p>
     * This class is automatically published as a separate message to the {@code metrics} log by the
     * Fluxzero Java SDK after completing the request.
     */
    @Value
    public static class Metric {
        /**
         * Number of facet fields included in the result.
         */
        int size;

        /**
         * Timestamp of result generation.
         */
        long timestamp;
    }

    public record ValueCountPair(String value, int count) {
        public ValueCountPair(FacetStats stat) {
            this(stat.getValue(), stat.getCount());
        }
    }
}