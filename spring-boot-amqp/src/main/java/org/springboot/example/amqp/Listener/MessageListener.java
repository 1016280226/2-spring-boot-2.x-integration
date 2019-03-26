package org.springboot.example.amqp.Listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Calvin
 * @titile: Message Endpoint
 * @date 2019/3/27
 * @since 1.0
 */
@Slf4j
@Component
public class MessageListener {

    /**
     * 使用场景:
     *    a.商品下单:
     *     1.下单-> rabbitListener 监听 -> 减少库存
     * @param recevieMessage
     */
    @RabbitListener(queues = {
            "Calvin"
    })
    public void rabbitListener(JSONObject recevieMessage){
        log.info("收到消息体:{}",recevieMessage);
    }
}
