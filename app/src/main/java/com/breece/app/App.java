package com.breece.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Slf4j
@EnableJpaRepositories(basePackages = "com.breece")
// Explicitly scan for JPA entities (@Entity) in all modules
@EntityScan(basePackages = "com.breece")
@ComponentScan(basePackages = "com.breece")
public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
        log.info("Application started successfully");
    }
}
