package org.springboot.example.amqp;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author Calvin
 * @titile:
 * @date 2019/3/27
 * @since 1.0
 *
 * RabbitAutoConfiguration:
 *  1.ConnectionFactory
 *    a.Configuration ConnectionFactory By RabbitProperties
 *    b.The RabbitTemplate Send And Receive Message To RabbitMQ
 *    c.AmqpAdmin Is RabbitMQ System Manager Function Component
 */
@EnableAutoConfiguration
@Configuration
@AutoConfigureBefore(RabbitAutoConfiguration.class)
public class RabbitMQAutoConfiguration {
}
