package com.imepac.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imepac.commons.dto.CustomerDTO;
import com.imepac.commons.enums.OrderStatus;
import com.imepac.commons.exception.FeignIntegrationException;
import com.imepac.commons.response.ApiResponse;
import com.imepac.order.client.CustomerClient;
import com.imepac.order.dto.CreateOrderItemRequest;
import com.imepac.order.dto.CreateOrderRequest;
import com.imepac.order.dto.UpdateOrderStatusRequest;
import com.imepac.order.entity.Order;
import com.imepac.order.entity.OrderItem;
import com.imepac.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@DisplayName("OrderController Integration Tests")
class OrderControllerIT {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("order_db_test")
            .withUsername("root")
            .withPassword("root");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private CustomerClient customerClient;

    private CustomerDTO activeCustomer;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();

        activeCustomer = CustomerDTO.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@email.com")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/orders - should create order and return 201")
    void shouldCreateOrder() throws Exception {
        when(customerClient.validateActiveCustomer(anyLong()))
                .thenReturn(ApiResponse.success(activeCustomer));

        CreateOrderRequest request = CreateOrderRequest.builder()
                .customerId(1L)
                .items(List.of(
                        CreateOrderItemRequest.builder()
                                .productName("Product X")
                                .quantity(2)
                                .unitPrice(new BigDecimal("29.99"))
                                .build()
                ))
                .build();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.customerId").value(1))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.totalAmount").value(59.98))
                .andExpect(jsonPath("$.data.items", hasSize(1)));
    }

    @Test
    @DisplayName("POST /api/v1/orders - should return 400 when request is invalid")
    void shouldReturn400WhenInvalid() throws Exception {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .customerId(null)
                .items(List.of())
                .build();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("POST /api/v1/orders - should return 502 when customer not found")
    void shouldReturn502WhenCustomerNotFound() throws Exception {
        when(customerClient.validateActiveCustomer(anyLong()))
                .thenThrow(new FeignIntegrationException("Customer not found"));

        CreateOrderRequest request = CreateOrderRequest.builder()
                .customerId(999L)
                .items(List.of(
                        CreateOrderItemRequest.builder()
                                .productName("Product Y")
                                .quantity(1)
                                .unitPrice(new BigDecimal("10.00"))
                                .build()
                ))
                .build();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/orders - should list all orders")
    void shouldListOrders() throws Exception {
        saveOrder(1L, OrderStatus.PENDING);
        saveOrder(2L, OrderStatus.CONFIRMED);

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    @DisplayName("GET /api/v1/orders/{id} - should return order")
    void shouldFindById() throws Exception {
        Order saved = saveOrder(1L, OrderStatus.PENDING);

        mockMvc.perform(get("/api/v1/orders/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(saved.getId()))
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    @DisplayName("GET /api/v1/orders/{id} - should return 404 when not found")
    void shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/orders/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/orders/customer/{customerId} - should list by customer")
    void shouldListByCustomer() throws Exception {
        saveOrder(1L, OrderStatus.PENDING);
        saveOrder(1L, OrderStatus.CONFIRMED);
        saveOrder(2L, OrderStatus.PENDING);

        mockMvc.perform(get("/api/v1/orders/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    @DisplayName("PATCH /api/v1/orders/{id}/status - should update status")
    void shouldUpdateStatus() throws Exception {
        Order saved = saveOrder(1L, OrderStatus.PENDING);

        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.builder()
                .status(OrderStatus.CONFIRMED)
                .build();

        mockMvc.perform(patch("/api/v1/orders/{id}/status", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CONFIRMED"));
    }

    private Order saveOrder(Long customerId, OrderStatus status) {
        OrderItem item = OrderItem.builder()
                .productName("Product Test")
                .quantity(1)
                .unitPrice(new BigDecimal("19.99"))
                .build();

        Order order = Order.builder()
                .customerId(customerId)
                .status(status)
                .totalAmount(new BigDecimal("19.99"))
                .items(new ArrayList<>())
                .build();

        order.addItem(item);
        return orderRepository.save(order);
    }
}
