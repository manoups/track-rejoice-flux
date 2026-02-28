package com.breece.content.command.api;

import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.ContentStatus;
import io.fluxzero.common.api.search.Constraint;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import static io.fluxzero.common.api.search.constraints.MatchConstraint.match;

public record GetContentStates(@NotNull @NotEmpty List<ContentId> ids, @NotNull @NotEmpty List<ContentStatus> statuses) implements Request<List<ContentState>> {
    //TODO: Add contentState query if necessary
    @HandleQuery
    List<ContentState> getState() {
        return Fluxzero.search(ContentState.class)
                .any(ids.stream().map(v -> match(v, "contentId")).toArray(Constraint[]::new))
                .any(statuses.stream().map(v -> match(v, "status")).toArray(Constraint[]::new))
                .fetchAll();
    }
}