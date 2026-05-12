package com.imepac.order.converter;

import com.imepac.commons.enums.OrderStatus;
import com.imepac.order.dto.CreateOrderItemRequest;
import com.imepac.order.dto.CreateOrderRequest;
import com.imepac.order.dto.OrderItemResponse;
import com.imepac.order.dto.OrderResponse;
import com.imepac.order.entity.Order;
import com.imepac.order.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public final class OrderConverter {

    private OrderConverter() {}

    public static Order toEntity(CreateOrderRequest request) {
        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        List<OrderItem> items = request.getItems().stream()
                .map(itemReq -> toItemEntity(itemReq, order))
                .collect(Collectors.toList());

        items.forEach(order::addItem);

        BigDecimal total = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
        return order;
    }

    public static OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(OrderConverter::toItemResponse)
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(itemResponses)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private static OrderItem toItemEntity(CreateOrderItemRequest request, Order order) {
        return OrderItem.builder()
                .productName(request.getProductName())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .order(order)
                .build();
    }

    private static OrderItemResponse toItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
