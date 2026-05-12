package com.imepac.order.service.impl;

import com.imepac.commons.enums.OrderStatus;
import com.imepac.commons.exception.BusinessException;
import com.imepac.order.client.CustomerClient;
import com.imepac.order.converter.OrderConverter;
import com.imepac.order.dto.CreateOrderRequest;
import com.imepac.order.dto.OrderResponse;
import com.imepac.order.entity.Order;
import com.imepac.order.exception.OrderNotFoundException;
import com.imepac.order.repository.OrderRepository;
import com.imepac.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;

    @Override
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        log.info("Creating order for customer id: {}", request.getCustomerId());

        customerClient.validateActiveCustomer(request.getCustomerId());

        Order order = OrderConverter.toEntity(request);
        Order saved = orderRepository.save(order);
        log.info("Order created with id: {}", saved.getId());
        return OrderConverter.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        log.info("Finding order by id: {}", id);
        Order order = orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return OrderConverter.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        log.info("Listing all orders");
        return orderRepository.findAll()
                .stream()
                .map(OrderConverter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByCustomerId(Long customerId) {
        log.info("Listing orders for customer id: {}", customerId);
        return orderRepository.findAllByCustomerId(customerId)
                .stream()
                .map(OrderConverter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        log.info("Updating order {} status to {}", id, status);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        validateStatusTransition(order.getStatus(), status);

        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return OrderConverter.toResponse(updated);
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        if (current == OrderStatus.CANCELLED || current == OrderStatus.DELIVERED) {
            throw new BusinessException(
                    "Cannot change status of an order in state: " + current);
        }
        if (next == OrderStatus.PENDING && current != OrderStatus.PENDING) {
            throw new BusinessException("Cannot revert order to PENDING status");
        }
    }
}
