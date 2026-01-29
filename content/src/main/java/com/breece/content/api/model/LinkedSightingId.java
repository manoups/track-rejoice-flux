package com.breece.content.api.model;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Id;

public class LinkedSightingId extends Id<LinkedSighting> {
    public LinkedSightingId(String functionalId) {super(functionalId, "ps-");}
    public LinkedSightingId() {this(Fluxzero.generateId());}
}
