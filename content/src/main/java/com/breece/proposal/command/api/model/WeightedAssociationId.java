package com.breece.proposal.command.api.model;

import com.breece.content.api.model.WeightedAssociation;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Id;

public class WeightedAssociationId extends Id<WeightedAssociation> {
    public WeightedAssociationId(String id) {super(id, "wa-");}
    public WeightedAssociationId() {this(Fluxzero.generateId());}
}
