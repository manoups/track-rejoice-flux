package com.breece.proposal.command.api.model;

import com.breece.content.api.model.ContentId;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.Id;

public class LinkedSightingId extends Id<LinkedSighting> {
    public LinkedSightingId(ContentId contentId, SightingId sightingId) {this(contentId.toString()+"-"+sightingId.toString());}
    private LinkedSightingId(String functionalId) {super(functionalId, "ls-");}
}
