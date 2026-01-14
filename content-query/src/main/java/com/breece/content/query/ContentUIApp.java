package com.breece.content.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ContentUIApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(ContentUIApp.class, args);
        log.info("Application started successfully");
    }
}
