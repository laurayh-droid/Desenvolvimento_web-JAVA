package com.imepac.order.service;

import com.imepac.commons.dto.CustomerDTO;
import com.imepac.commons.enums.OrderStatus;
import com.imepac.commons.exception.BusinessException;
import com.imepac.commons.exception.FeignIntegrationException;
import com.imepac.commons.response.ApiResponse;
import com.imepac.order.client.CustomerClient;
import com.imepac.order.dto.CreateOrderItemRequest;
import com.imepac.order.dto.CreateOrderRequest;
import com.imepac.order.dto.OrderResponse;
import com.imepac.order.entity.Order;
import com.imepac.order.entity.OrderItem;
import com.imepac.order.exception.OrderNotFoundException;
import com.imepac.order.repository.OrderRepository;
import com.imepac.order.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Unit Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerClient customerClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private CreateOrderRequest createRequest;
    private CustomerDTO activeCustomer;

    @BeforeEach
    void setUp() {
        OrderItem item = OrderItem.builder()
                .id(1L)
                .productName("Product A")
                .quantity(2)
                .unitPrice(new BigDecimal("49.90"))
                .build();

        order = Order.builder()
                .id(1L)
                .customerId(1L)
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("99.80"))
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>(List.of(item)))
                .build();
        item.setOrder(order);

        createRequest = CreateOrderRequest.builder()
                .customerId(1L)
                .items(List.of(CreateOrderItemRequest.builder()
                        .productName("Product A")
                        .quantity(2)
                        .unitPrice(new BigDecimal("49.90"))
                        .build()))
                .build();

        activeCustomer = CustomerDTO.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@email.com")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should create order successfully when customer is active")
    void shouldCreateOrder() {
        when(customerClient.validateActiveCustomer(1L))
                .thenReturn(ApiResponse.success(activeCustomer));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.create(createRequest);

        assertThat(response).isNotNull();
        assertThat(response.getCustomerId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(OrderStatus.PENDING);
        verify(customerClient).validateActiveCustomer(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw FeignIntegrationException when customer not found")
    void shouldThrowWhenCustomerNotFound() {
        when(customerClient.validateActiveCustomer(99L))
                .thenThrow(new FeignIntegrationException("Customer not found"));

        CreateOrderRequest req = CreateOrderRequest.builder()
                .customerId(99L)
                .items(createRequest.getItems())
                .build();

        assertThatThrownBy(() -> orderService.create(req))
                .isInstanceOf(FeignIntegrationException.class)
                .hasMessageContaining("Customer not found");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw FeignIntegrationException when customer is inactive")
    void shouldThrowWhenCustomerInactive() {
        when(customerClient.validateActiveCustomer(1L))
                .thenThrow(new FeignIntegrationException("Customer is not active"));

        assertThatThrownBy(() -> orderService.create(createRequest))
                .isInstanceOf(FeignIntegrationException.class)
                .hasMessageContaining("not active");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find order by id with items")
    void shouldFindById() {
        when(orderRepository.findByIdWithItems(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.findById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getItems()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw OrderNotFoundException when order not found")
    void shouldThrowWhenOrderNotFound() {
        when(orderRepository.findByIdWithItems(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.findById(99L))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("Should list all orders")
    void shouldListAll() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderResponse> result = orderService.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Should list orders by customer")
    void shouldListByCustomer() {
        when(orderRepository.findAllByCustomerId(1L)).thenReturn(List.of(order));

        List<OrderResponse> result = orderService.findByCustomerId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomerId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should update order status")
    void shouldUpdateStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.updateStatus(1L, OrderStatus.CONFIRMED);

        assertThat(response).isNotNull();
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw when updating status of CANCELLED order")
    void shouldThrowWhenUpdatingCancelledOrder() {
        order.setStatus(OrderStatus.CANCELLED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateStatus(1L, OrderStatus.CONFIRMED))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("CANCELLED");
    }

    @Test
    @DisplayName("Should throw when reverting to PENDING status")
    void shouldThrowWhenRevertingToPending() {
        order.setStatus(OrderStatus.CONFIRMED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateStatus(1L, OrderStatus.PENDING))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("PENDING");
    }
}
