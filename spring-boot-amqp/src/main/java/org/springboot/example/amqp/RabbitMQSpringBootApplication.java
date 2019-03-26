package org.springboot.example.amqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Calvin
 * @titile:
 * @date 2019/3/27
 * @since 1.0
 */
@EnableSwagger2
@SpringBootApplication
public class RabbitMQSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQSpringBootApplication.class, args);
    }
}
