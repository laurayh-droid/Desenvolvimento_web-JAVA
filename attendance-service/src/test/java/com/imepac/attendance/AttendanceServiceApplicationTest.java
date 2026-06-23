package com.imepac.attendance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AttendanceServiceApplication.class)
@org.springframework.test.context.ActiveProfiles("test")
class AttendanceServiceApplicationTest {

    @Test
    void contextLoads() {
        // smoke test: confirma que o contexto da aplicação carrega
    }
}
