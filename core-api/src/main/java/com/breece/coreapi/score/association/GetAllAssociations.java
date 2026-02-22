package com.breece.coreapi.score.association;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

import java.util.List;

public record GetAllAssociations() implements Request<List<WeightedAssociation>> {
    @HandleQuery
    List<WeightedAssociation> find() {
        return Fluxzero.search(WeightedAssociation.class).fetchAll();
    }
}
