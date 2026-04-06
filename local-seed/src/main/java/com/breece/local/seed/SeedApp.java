package com.breece.local.seed;

import io.fluxzero.sdk.configuration.spring.FluxzeroSpringConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@Import(FluxzeroSpringConfig.class)
public class SeedApp
{
    public static void main( String[] args )
    {
        int exitCode = new SeedRunner().run();
        System.exit(exitCode);
    }
}
