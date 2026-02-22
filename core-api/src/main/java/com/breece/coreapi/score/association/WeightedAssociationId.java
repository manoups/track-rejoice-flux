package com.breece.coreapi.score.association;

import io.fluxzero.sdk.modeling.Id;

public class WeightedAssociationId extends Id<WeightedAssociation> {

    public WeightedAssociationId(String functionalId) {
        super(functionalId, "sa-");
    }

    public WeightedAssociationId(String contentId, String sightingId) {
        this(String.format("%s-%s", contentId, sightingId));
    }


}
