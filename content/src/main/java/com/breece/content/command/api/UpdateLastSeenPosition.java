package com.breece.content.command.api;

import com.breece.content.api.ConfirmedSightingUpdate;
import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.RequiresRole;
import com.breece.coreapi.authentication.Role;
import com.breece.coreapi.common.SightingDetails;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

@RequiresRole(Role.ADMIN)
public record UpdateLastSeenPosition(ContentId contentId, SightingDetails sightingDetails) implements ContentUpdate, ConfirmedSightingUpdate {
    @Apply
    Content apply(Content content) {
        return content.withLastConfirmedSighting(sightingDetails);
    }
}
