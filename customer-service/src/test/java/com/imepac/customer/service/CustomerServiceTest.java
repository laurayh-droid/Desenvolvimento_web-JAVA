package com.imepac.customer.service;

import com.imepac.commons.dto.CustomerDTO;
import com.imepac.commons.exception.BusinessException;
import com.imepac.customer.dto.CreateCustomerRequest;
import com.imepac.customer.dto.CustomerResponse;
import com.imepac.customer.dto.UpdateCustomerRequest;
import com.imepac.customer.entity.Customer;
import com.imepac.customer.exception.CustomerNotFoundException;
import com.imepac.customer.repository.CustomerRepository;
import com.imepac.customer.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService Unit Tests")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CreateCustomerRequest createRequest;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("João Silva")
                .email("joao@email.com")
                .phone("11999999999")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        createRequest = CreateCustomerRequest.builder()
                .name("João Silva")
                .email("joao@email.com")
                .phone("11999999999")
                .build();
    }

    @Test
    @DisplayName("Should create customer successfully")
    void shouldCreateCustomer() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse response = customerService.create(createRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("João Silva");
        assertThat(response.getEmail()).isEqualTo("joao@email.com");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when email already exists")
    void shouldThrowWhenEmailExists() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> customerService.create(createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already exists");

        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find customer by id")
    void shouldFindById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponse response = customerService.findById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when not found")
    void shouldThrowWhenNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findById(99L))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    @DisplayName("Should list all customers")
    void shouldListAll() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<CustomerResponse> result = customerService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("Should list only active customers")
    void shouldListAllActive() {
        when(customerRepository.findAllByActiveTrue()).thenReturn(List.of(customer));

        List<CustomerResponse> result = customerService.findAllActive();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActive()).isTrue();
    }

    @Test
    @DisplayName("Should update customer")
    void shouldUpdateCustomer() {
        UpdateCustomerRequest updateRequest = UpdateCustomerRequest.builder()
                .name("João Updated")
                .phone("11888888888")
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse response = customerService.update(1L, updateRequest);

        assertThat(response).isNotNull();
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should delete customer")
    void shouldDeleteCustomer() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        assertThatCode(() -> customerService.delete(1L)).doesNotThrowAnyException();
        verify(customerRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw when deleting non-existent customer")
    void shouldThrowWhenDeletingNonExistent() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> customerService.delete(99L))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    @DisplayName("Should validate active customer")
    void shouldValidateActiveCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDTO dto = customerService.validateActive(1L);

        assertThat(dto).isNotNull();
        assertThat(dto.getActive()).isTrue();
    }

    @Test
    @DisplayName("Should throw BusinessException when customer is inactive")
    void shouldThrowWhenCustomerInactive() {
        customer.setActive(false);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> customerService.validateActive(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("not active");
    }
}
