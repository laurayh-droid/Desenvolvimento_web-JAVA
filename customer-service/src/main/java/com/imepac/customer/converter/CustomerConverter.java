package com.imepac.customer.converter;

import com.imepac.commons.dto.CustomerDTO;
import com.imepac.customer.dto.CreateCustomerRequest;
import com.imepac.customer.dto.CustomerResponse;
import com.imepac.customer.entity.Customer;

public final class CustomerConverter {

    private CustomerConverter() {}

    public static Customer toEntity(CreateCustomerRequest request) {
        return Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .active(true)
                .build();
    }

    public static CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .active(customer.getActive())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    public static CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .active(customer.getActive())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}
