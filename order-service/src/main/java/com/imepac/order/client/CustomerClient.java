package com.imepac.order.client;

import com.imepac.commons.dto.CustomerDTO;
import com.imepac.commons.response.ApiResponse;
import com.imepac.order.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "customer-service",
        url = "${customer-service.url}",
        configuration = FeignConfig.class
)
public interface CustomerClient {

    @GetMapping("/api/v1/customers/{id}/validate-active")
    ApiResponse<CustomerDTO> validateActiveCustomer(@PathVariable("id") Long id);
}
