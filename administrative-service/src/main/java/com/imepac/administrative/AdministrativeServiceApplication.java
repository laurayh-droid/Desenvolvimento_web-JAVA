package com.imepac.administrative;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.imepac")
@EntityScan(basePackages = "com.imepac.commons.entity")
public class AdministrativeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdministrativeServiceApplication.class, args);
    }
}
