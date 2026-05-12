package com.imepac.customer.controller;

import com.imepac.commons.dto.CustomerDTO;
import com.imepac.commons.response.ApiResponse;
import com.imepac.customer.dto.CreateCustomerRequest;
import com.imepac.customer.dto.CustomerResponse;
import com.imepac.customer.dto.UpdateCustomerRequest;
import com.imepac.customer.service.CustomerService;
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
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Customer management endpoints")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new customer")
    public ApiResponse<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
        CustomerResponse response = customerService.create(request);
        return ApiResponse.success(response, "Customer created successfully");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    public ApiResponse<CustomerResponse> findById(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        return ApiResponse.success(customerService.findById(id));
    }

    @GetMapping
    @Operation(summary = "List all customers")
    public ApiResponse<List<CustomerResponse>> findAll(
            @RequestParam(required = false, defaultValue = "false") Boolean activeOnly) {
        List<CustomerResponse> customers = Boolean.TRUE.equals(activeOnly)
                ? customerService.findAllActive()
                : customerService.findAll();
        return ApiResponse.success(customers);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    public ApiResponse<CustomerResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        return ApiResponse.success(customerService.update(id, request), "Customer updated successfully");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete customer")
    public void delete(@PathVariable Long id) {
        customerService.delete(id);
    }

    @GetMapping("/{id}/validate-active")
    @Operation(summary = "Validate if customer is active (used by order-service)")
    public ApiResponse<CustomerDTO> validateActive(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        return ApiResponse.success(customerService.validateActive(id));
    }
}
