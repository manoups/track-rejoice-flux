package com.breece.proposal.command.api;

import com.breece.content.api.model.ContentId;
import com.breece.proposal.command.api.model.LinkedSightingId;
import com.breece.proposal.command.api.model.LinkedSightingUpdateOwner;

public record AcceptProposal(ContentId contentId, LinkedSightingId linkedSightingId) implements LinkedSightingUpdateOwner {
}
