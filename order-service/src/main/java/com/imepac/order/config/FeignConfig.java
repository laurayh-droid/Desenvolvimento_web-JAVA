package com.imepac.order.config;

import com.imepac.commons.exception.FeignIntegrationException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomerFeignErrorDecoder();
    }

    static class CustomerFeignErrorDecoder implements ErrorDecoder {

        private final ErrorDecoder defaultDecoder = new Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            log.error("Feign error - method: {}, status: {}", methodKey, response.status());
            return switch (response.status()) {
                case 404 -> new FeignIntegrationException("Customer not found");
                case 422 -> new FeignIntegrationException("Customer is not active");
                case 503 -> new FeignIntegrationException("Customer service is unavailable");
                default -> defaultDecoder.decode(methodKey, response);
            };
        }
    }
}
