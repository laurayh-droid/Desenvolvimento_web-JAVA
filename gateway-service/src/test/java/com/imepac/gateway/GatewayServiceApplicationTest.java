package com.imepac.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = GatewayServiceApplication.class)
@org.springframework.test.context.ActiveProfiles("test")
class GatewayServiceApplicationTest {

    @Test
    void contextLoads() {
        // smoke test: confirma que o contexto da aplicação carrega
    }
}
