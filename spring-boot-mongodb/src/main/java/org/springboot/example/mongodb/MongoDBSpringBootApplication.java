package org.springboot.example.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Calvin
 * @titile: @EnableMongoRepositories
 * @date 2019/2/28
 * @since 1.0
 * @annonation
 *   1. @EnableMongoRepositories : 指定扫描的包，用于扫描继承了MongoRepository 的接口
 *      a.basePackages: 扫描包
 *      b.repositoryImplementationPostfix: 实现 Repository 接口，自定义类的后缀名称。默认impl
 */
@EnableSwagger2
@EnableSpringDataWebSupport
@EnableMongoRepositories(
        basePackages = "org.springboot.example.mongodb.repository",
        repositoryImplementationPostfix = "impl"
)
@SpringBootApplication(
        scanBasePackages = "org.springboot.example.mongodb")
public class MongoDBSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoDBSpringBootApplication.class, args);
    }
}
