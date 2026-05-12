package com.imepac.order.controller;

import com.imepac.commons.response.ApiResponse;
import com.imepac.order.dto.CreateOrderRequest;
import com.imepac.order.dto.OrderResponse;
import com.imepac.order.dto.UpdateOrderStatusRequest;
import com.imepac.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new order")
    public ApiResponse<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.create(request);
        return ApiResponse.success(response, "Order created successfully");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ApiResponse<OrderResponse> findById(
            @Parameter(description = "Order ID") @PathVariable Long id) {
        return ApiResponse.success(orderService.findById(id));
    }

    @GetMapping
    @Operation(summary = "List all orders")
    public ApiResponse<List<OrderResponse>> findAll() {
        return ApiResponse.success(orderService.findAll());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "List orders by customer")
    public ApiResponse<List<OrderResponse>> findByCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        return ApiResponse.success(orderService.findByCustomerId(customerId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public ApiResponse<OrderResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse response = orderService.updateStatus(id, request.getStatus());
        return ApiResponse.success(response, "Order status updated successfully");
    }
}
