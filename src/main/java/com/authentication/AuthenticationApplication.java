package com.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class AuthenticationApplication {

    public static final Logger logger = LoggerFactory.getLogger(AuthenticationApplication.class);

    public static void main(String[] args) {
        logger.info("Application started.");
        SpringApplication.run(AuthenticationApplication.class, args);
        logger.info("Application ended.");
    }

}
