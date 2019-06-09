package org.springboot.example.webmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Calvin
 * @titile: Spring Boot Web MVC Application starter
 * @date 2019/3/7
 * @since 1.0
 */
@SpringBootApplication
@EnableAutoConfiguration
public class WebMVCSpringBootApplication {

        public static void main(String[] args) {
            SpringApplication.run(WebMVCSpringBootApplication.class, args);
        }
}
