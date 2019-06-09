package org.springboot.example.example.mongodb;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author Calvin
 * @titile:
 * @date 2019/3/1
 * @since 1.0
 */
@EnableAutoConfiguration
@Configuration
@AutoConfigureBefore(MongoDataAutoConfiguration.class)
public class MongoDBAutoConfiguration {
}
