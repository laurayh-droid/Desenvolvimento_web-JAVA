package com.imepac.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import static java.lang.annotation.ElementType.*;

@SpringBootApplication(scanBasePackages = "com.imepac")
@EntityScan(basePackages = "com.imepac.commons.entity")
public class AppointmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointmentServiceApplication.class, args);
    }
}


