package com.github.bogdanovmn.memorydeluge.viewer.web.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.github.bogdanovmn.memorydeluge.viewer.model")
@EntityScan(basePackages = "com.github.bogdanovmn.memorydeluge.viewer.model")
@ComponentScan(basePackages = "com.github.bogdanovmn.memorydeluge.viewer")
@EnableTransactionManagement
@EnableScheduling
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

