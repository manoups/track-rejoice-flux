package com.breece.common.model;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Id;

public class ProposedSightingId extends Id<ProposedSighting> {
    public ProposedSightingId(String functionalId) {super(functionalId, "ps-");}
    public ProposedSightingId() {this(Fluxzero.generateId());}
}
