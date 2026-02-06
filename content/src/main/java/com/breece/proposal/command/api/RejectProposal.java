package com.breece.proposal.command.api;

import com.breece.content.api.model.ContentId;
import com.breece.proposal.command.api.model.LinkedSightingUpdateOwner;
import com.breece.proposal.command.api.model.LinkedSightingId;

public record RejectProposal(ContentId contentId, LinkedSightingId linkedSightingId) implements LinkedSightingUpdateOwner {
}