package com.breece.local.seed;

import io.fluxzero.sdk.configuration.spring.FluxzeroSpringConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication
@Import(FluxzeroSpringConfig.class)
public class SeedApp {

    public static void main(String[] args) {
        SpringApplication.run(SeedApp.class, args);
        int exitCode = 0;
        try {
            new SeedPlan().seed();
            log.info("Seeding completed successfully");
        } catch (Exception e) {
            log.error("Seeding failed", e);
            exitCode = 1;
        }
        forceExit(exitCode);
    }

    private static void forceExit(int code) {
        Runtime.getRuntime().halt(code);
    }
}
