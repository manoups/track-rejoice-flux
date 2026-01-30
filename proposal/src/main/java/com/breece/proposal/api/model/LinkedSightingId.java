package com.breece.proposal.api.model;

import com.breece.content.api.model.ContentId;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Id;

public class LinkedSightingId extends Id<LinkedSighting> {
    public LinkedSightingId(ContentId contentId, SightingId sightingId) {super(contentId.getId()+"-"+sightingId.getId());}
    private LinkedSightingId(String functionalId) {super(functionalId, "ls-");}
    public LinkedSightingId() {this(Fluxzero.generateId());}
}
