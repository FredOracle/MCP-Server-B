package com.example.mcpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class McpserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpserverApplication.class, args);
    }

}
