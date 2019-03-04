package org.springboot.example.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Calvin
 * @titile: Swagger Spring Boot Application
 * @date 2019/3/1
 * @since 1.0
 */

@EnableAutoConfiguration
@EnableConfigurationProperties
@SpringBootApplication
public class SwaggerSpringBootApplication {

    public static void main(String[] args) {

        SpringApplication.run(SwaggerSpringBootApplication.class, args);
    }
}
