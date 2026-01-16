package com.breece.order.api.model;

import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.sdk.modeling.Aggregate;
import jakarta.validation.constraints.NotNull;
import lombok.With;

import static io.fluxzero.sdk.modeling.EventPublication.IF_MODIFIED;

@Aggregate(searchable = true, eventPublication = IF_MODIFIED)
@With
public record Order(OrderId orderId, UserId userId, @NotNull ContentId contentId, OrderDetails details, @With String status, @With boolean aborted) {
}
