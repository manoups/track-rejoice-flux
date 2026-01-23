package com.breece.order.api.order.model;

import com.breece.content.api.model.ContentId;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.With;

@Aggregate(searchable = true)
public record Order(@EntityId OrderId orderId, @NotNull UserId userId, @NotNull ContentId contentId, @Valid OrderDetails details, @With String paymentReference) {
}
