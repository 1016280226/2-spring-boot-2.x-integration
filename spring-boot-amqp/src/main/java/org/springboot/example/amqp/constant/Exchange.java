package org.springboot.example.amqp.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Exchange {
        /**
         * 点对点
         */
        direct("amq.direct"),
        /**
         * 广播订阅和发送
         */
        fanout("amq.fanout"),
        /**
         * 路由机制发送和接受
         */
        topic("amq.topic"),
        /**
         *
         */
        headers("amq.headers"),;

        private String message;
    }