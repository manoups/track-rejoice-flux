package com.breece.content.command.api;

import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.order.model.OrderId;
import jakarta.validation.constraints.NotNull;

public class PublishContent {
    public PublishContent(@NotNull ContentId contentId, OrderId orderId) {
    }
}
