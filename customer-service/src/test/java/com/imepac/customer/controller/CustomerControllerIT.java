package com.imepac.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imepac.customer.dto.CreateCustomerRequest;
import com.imepac.customer.dto.UpdateCustomerRequest;
import com.imepac.customer.entity.Customer;
import com.imepac.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@DisplayName("CustomerController Integration Tests")
class CustomerControllerIT {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("customer_db_test")
            .withUsername("root")
            .withPassword("root");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/v1/customers - should create customer and return 201")
    void shouldCreateCustomer() throws Exception {
        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .name("Maria Souza")
                .email("maria@email.com")
                .phone("11977777777")
                .build();

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Maria Souza"))
                .andExpect(jsonPath("$.data.email").value("maria@email.com"))
                .andExpect(jsonPath("$.data.active").value(true));
    }

    @Test
    @DisplayName("POST /api/v1/customers - should return 400 when request is invalid")
    void shouldReturn400WhenInvalid() throws Exception {
        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .name("")
                .email("not-an-email")
                .build();

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("POST /api/v1/customers - should return 422 when email already exists")
    void shouldReturn422WhenEmailExists() throws Exception {
        Customer saved = customerRepository.save(Customer.builder()
                .name("Existing User")
                .email("existing@email.com")
                .active(true)
                .build());

        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .name("New User")
                .email(saved.getEmail())
                .build();

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/customers - should list all customers")
    void shouldListCustomers() throws Exception {
        customerRepository.save(Customer.builder().name("A").email("a@a.com").active(true).build());
        customerRepository.save(Customer.builder().name("B").email("b@b.com").active(true).build());

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id} - should return customer")
    void shouldFindById() throws Exception {
        Customer saved = customerRepository.save(Customer.builder()
                .name("Test User")
                .email("test@email.com")
                .active(true)
                .build());

        mockMvc.perform(get("/api/v1/customers/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(saved.getId()))
                .andExpect(jsonPath("$.data.name").value("Test User"));
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id} - should return 404 when not found")
    void shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/customers/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id}/validate-active - should validate active customer")
    void shouldValidateActiveCustomer() throws Exception {
        Customer saved = customerRepository.save(Customer.builder()
                .name("Active User")
                .email("active@email.com")
                .active(true)
                .build());

        mockMvc.perform(get("/api/v1/customers/{id}/validate-active", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.active").value(true));
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id}/validate-active - should return 422 when inactive")
    void shouldReturn422WhenInactive() throws Exception {
        Customer saved = customerRepository.save(Customer.builder()
                .name("Inactive User")
                .email("inactive@email.com")
                .active(false)
                .build());

        mockMvc.perform(get("/api/v1/customers/{id}/validate-active", saved.getId()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("PUT /api/v1/customers/{id} - should update customer")
    void shouldUpdateCustomer() throws Exception {
        Customer saved = customerRepository.save(Customer.builder()
                .name("Original Name")
                .email("original@email.com")
                .active(true)
                .build());

        UpdateCustomerRequest update = UpdateCustomerRequest.builder()
                .name("Updated Name")
                .phone("11966666666")
                .build();

        mockMvc.perform(put("/api/v1/customers/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Name"));
    }

    @Test
    @DisplayName("DELETE /api/v1/customers/{id} - should delete customer")
    void shouldDeleteCustomer() throws Exception {
        Customer saved = customerRepository.save(Customer.builder()
                .name("To Delete")
                .email("delete@email.com")
                .active(true)
                .build());

        mockMvc.perform(delete("/api/v1/customers/{id}", saved.getId()))
                .andExpect(status().isNoContent());
    }
}
