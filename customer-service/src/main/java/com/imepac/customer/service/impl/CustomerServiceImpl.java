package com.imepac.customer.service.impl;

import com.imepac.commons.dto.CustomerDTO;
import com.imepac.commons.exception.BusinessException;
import com.imepac.customer.converter.CustomerConverter;
import com.imepac.customer.dto.CreateCustomerRequest;
import com.imepac.customer.dto.CustomerResponse;
import com.imepac.customer.dto.UpdateCustomerRequest;
import com.imepac.customer.entity.Customer;
import com.imepac.customer.exception.CustomerNotFoundException;
import com.imepac.customer.repository.CustomerRepository;
import com.imepac.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerResponse create(CreateCustomerRequest request) {
        log.info("Creating customer with email: {}", request.getEmail());
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Customer already exists with email: " + request.getEmail());
        }
        Customer customer = CustomerConverter.toEntity(request);
        Customer saved = customerRepository.save(customer);
        log.info("Customer created with id: {}", saved.getId());
        return CustomerConverter.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        log.info("Finding customer by id: {}", id);
        return customerRepository.findById(id)
                .map(CustomerConverter::toResponse)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        log.info("Listing all customers");
        return customerRepository.findAll()
                .stream()
                .map(CustomerConverter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAllActive() {
        log.info("Listing all active customers");
        return customerRepository.findAllByActiveTrue()
                .stream()
                .map(CustomerConverter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerResponse update(Long id, UpdateCustomerRequest request) {
        log.info("Updating customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (request.getName() != null) {
            customer.setName(request.getName());
        }
        if (request.getPhone() != null) {
            customer.setPhone(request.getPhone());
        }
        if (request.getActive() != null) {
            customer.setActive(request.getActive());
        }

        Customer updated = customerRepository.save(customer);
        return CustomerConverter.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting customer with id: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO validateActive(Long id) {
        log.info("Validating active customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (!Boolean.TRUE.equals(customer.getActive())) {
            throw new BusinessException("Customer with id " + id + " is not active");
        }
        return CustomerConverter.toDTO(customer);
    }
}
