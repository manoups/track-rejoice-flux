package com.breece.coreapi.facets;

import io.fluxzero.common.api.JsonType;
import io.fluxzero.common.api.search.FacetStats;
import lombok.Value;

import java.util.List;

@Value
public class GetFacetStatsResult implements JsonType {

    public GetFacetStatsResult(List<FacetStats> stats) {
        this.stats = stats.stream().map(facetStats -> new FacetStatsLocal(facetStats.getName(), facetStats.getValue(), facetStats.getCount())).toList();
    }

    List<FacetStatsLocal> stats;

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

    public record FacetStatsLocal(String name, String value, int count) {
    }
}