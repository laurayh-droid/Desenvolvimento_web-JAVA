package com.imepac.order.service;

import com.imepac.commons.enums.OrderStatus;
import com.imepac.order.dto.CreateOrderRequest;
import com.imepac.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse create(CreateOrderRequest request);

    OrderResponse findById(Long id);

    List<OrderResponse> findAll();

    List<OrderResponse> findByCustomerId(Long customerId);

    OrderResponse updateStatus(Long id, OrderStatus status);
}
