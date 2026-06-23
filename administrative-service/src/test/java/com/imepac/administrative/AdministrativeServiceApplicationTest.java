package com.imepac.administrative;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AdministrativeServiceApplication.class)
@org.springframework.test.context.ActiveProfiles("test")
class AdministrativeServiceApplicationTest {

    @Test
    void contextLoads() {
        // smoke test: confirma que o contexto da aplicação carrega
    }
}
