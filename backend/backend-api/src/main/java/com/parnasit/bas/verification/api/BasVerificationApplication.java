package com.parnasit.bas.verification.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.parnasit.bas.verification")
@EntityScan("com.parnasit.bas.verification.persistence.entity")
@EnableJpaRepositories("com.parnasit.bas.verification.persistence.repository")
public class BasVerificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasVerificationApplication.class, args);
    }
}
