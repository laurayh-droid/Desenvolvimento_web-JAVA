package com.imepac.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.imepac")
@EntityScan(basePackages = {
        "com.imepac.commons.entity",
        "com.imepac.attendance.entity"
})
public class AttendanceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceServiceApplication.class, args);
    }
}

