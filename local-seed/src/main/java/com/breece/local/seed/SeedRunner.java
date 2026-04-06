package com.breece.local.seed;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SeedRunner {

    public int run() {
        try {
            waitForRuntime();
            new SeedPlan().seed();
            log.info("Seeding completed successfully");
            return 0;
        } catch (Exception e) {
            log.error("Seeding failed", e);
            return 1;
        }
    }

    private void waitForRuntime() throws InterruptedException {
        // poll a readiness signal or retry a cheap query/command
    }
}

