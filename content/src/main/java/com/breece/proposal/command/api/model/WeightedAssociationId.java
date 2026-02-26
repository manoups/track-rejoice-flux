package com.breece.proposal.command.api.model;

import com.breece.content.api.model.ContentId;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.Id;

public class WeightedAssociationId extends Id<WeightedAssociation> {
    public WeightedAssociationId(ContentId contentId, SightingId sightingId) {this(contentId.toString()+"-"+sightingId.toString());}
    private WeightedAssociationId(String functionalId) {super(functionalId, "ls-");}
}
