package com.breece.order.api.order;

import com.breece.content.command.api.DeleteContent;
import com.breece.order.api.command.DeleteOrder;
import com.breece.order.api.order.model.Order;
import com.breece.order.api.order.model.OrderId;
import com.breece.order.api.payment.OrderProcess;
import com.breece.order.api.payment.OrderStatus;
import io.fluxzero.common.api.search.Constraint;
import io.fluxzero.common.api.search.constraints.MatchConstraint;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static io.fluxzero.common.api.search.constraints.MatchConstraint.match;

@Component
@Consumer(name = "order-handler")
public class OrderHandler {
    @HandleEvent
    void handle(DeleteContent event) {
        List<Order> matchingOrders = Fluxzero.search(Order.class)
                .match(event.contentId(), "contentId")
                .fetchAll();
        List<OrderId> createdOrders = Fluxzero.search(OrderProcess.class)
                .any(matchingOrders.stream().map(v -> match(v.orderId(), "orderId")).toArray(Constraint[]::new))
                .match(OrderStatus.CREATED, "status")
                .includeOnly("orderId")
                .<OrderProcess>stream().map(OrderProcess::orderId).toList();
        matchingOrders.stream().map(Order::orderId).filter(createdOrders::contains)
                .map(DeleteOrder::new)
                .forEach(Fluxzero::sendAndForgetCommand);
    }
}
