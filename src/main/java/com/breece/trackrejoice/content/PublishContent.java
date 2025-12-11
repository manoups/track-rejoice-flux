package com.breece.trackrejoice.content;

import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.orders.api.model.OrderId;
import jakarta.validation.constraints.NotNull;

public class PublishContent {
    public PublishContent(@NotNull ContentId contentId, OrderId orderId) {
    }
}
