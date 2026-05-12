package com.imepac.customer.service;

import com.imepac.commons.dto.CustomerDTO;
import com.imepac.customer.dto.CreateCustomerRequest;
import com.imepac.customer.dto.CustomerResponse;
import com.imepac.customer.dto.UpdateCustomerRequest;

import java.util.List;

public interface CustomerService {

    CustomerResponse create(CreateCustomerRequest request);

    CustomerResponse findById(Long id);

    List<CustomerResponse> findAll();

    List<CustomerResponse> findAllActive();

    CustomerResponse update(Long id, UpdateCustomerRequest request);

    void delete(Long id);

    CustomerDTO validateActive(Long id);
}
