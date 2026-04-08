package com.breece.local.seed;

import io.fluxzero.sdk.configuration.spring.FluxzeroSpringConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication
@Import(FluxzeroSpringConfig.class)
public class SeedApp implements CommandLineRunner {

    private final ApplicationContext context;

    public SeedApp(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(SeedApp.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            new SeedPlan().seed();
            log.info("Seeding completed successfully");
        } catch (Exception e) {
            log.error("Seeding failed", e);
        } finally {
            System.exit(SpringApplication.exit(context));
        }
    }
}
