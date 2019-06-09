package org.springboot.example.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Auther: Calvin
 * @Date: 2019/6/4
 * @Description:
 */
@EnableSwagger2
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "org.springboot.example.elasticsearch.respository")
public class ElasticsearchSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchSpringBootApplication.class, args);
    }
}
