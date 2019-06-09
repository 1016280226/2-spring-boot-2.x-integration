package org.springboot.example.example.amqp.listener;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Calvin
 * @titile: Message Listener
 * @date 2019/3/27
 * @since 1.0
 */
@Slf4j
@Component
public class MessageListener {

    /**
     *
     * 使用场景:
     *    a.商品下单:
     *     1.下单-> rabbitListener 监听 -> 减少库存
     *
     * Method 1 :
     * @param msg 消息体字节数组
     */
    @RabbitListener(queues = {
            "Calvin"
    })
    public void recevieMessage(Message msg){
        log.info("收到消息体字节数组:{}\n 消息属性:{}",msg.getBody(),msg.getMessageProperties());
    }

    /**
     * Method 2 :
     * @param msg json
     */
    @RabbitListener(queues = {
            "Calvin"
    })
    public void recevieMessage(JSONObject msg){
        log.info("收到消息体:{}",msg);
    }
}
