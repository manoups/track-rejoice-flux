package com.breece.app;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModularityTests {
    ApplicationModules modules = ApplicationModules.of(TestApp.class);

    @Test
    void verifyModularity() {
        modules.verify();
    }
}
