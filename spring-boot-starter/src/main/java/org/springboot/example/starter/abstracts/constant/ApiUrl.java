package org.springboot.example.example.starter.abstracts.constant;

/**
 * @author Calvin
 * @titile: API URL
 * @date 2019/2/28
 * @since 1.0
 */
public abstract class ApiUrl {
    public static final String SPRING_BOOT = "springboot";
    public static final String VESION = "v1";
    public static final String MONGODB = SPRING_BOOT + "/" + VESION + "/" + "mongodb";
    public static final String MONGODB_JPA = SPRING_BOOT + "/" + VESION + "/" + "mongodb" + "/" + "jpa";
    public static final String RABBITMQ = SPRING_BOOT + "/" + VESION + "/" + "amqp";
}
