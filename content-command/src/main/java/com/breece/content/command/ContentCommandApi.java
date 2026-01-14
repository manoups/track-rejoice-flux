package com.breece.content.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ContentCommandApi
{
    public static void main( String[] args )
    {
        SpringApplication.run(ContentCommandApi.class, args);
        log.info("Application started successfully");
    }
}
