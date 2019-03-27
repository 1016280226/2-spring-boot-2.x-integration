package org.springboot.example.amqp.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springboot.example.amqp.body.resquest.SendMessageRequestBody;
import org.springboot.example.amqp.constant.Exchange;
import org.springboot.example.starter.abstracts.constant.ApiUrl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Calvin
 * @titile: Message Endpoint
 * @date 2019/3/27
 * @since 1.0
 */
@Slf4j
@Api(value = "RabbitMQ",tags = {"RabbitMQ API"})
@RestController
@RequestMapping(ApiUrl.RABBITMQ + "/message")
public class MessageEndpoint {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ApiOperation(value = "direct", notes = "单播(点对点)", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("send/direct")
    public String sendDirect(@RequestBody @Validated SendMessageRequestBody sendMessageRequestBody){
        if(sendMessageRequestBody.getDefaultExchange() != null){
            if(sendMessageRequestBody.getDefaultExchange() != Exchange.direct){
                return new StringBuffer().append(sendMessageRequestBody.getDefaultExchange().name()).append("is paramter error").toString();
            }else {
                // 自动序列化发送
                rabbitTemplate.convertAndSend(sendMessageRequestBody.getDefaultExchange().getMessage(),sendMessageRequestBody.getRouteKey(),sendMessageRequestBody.getMessage());
            }
        }else if(StringUtils.isEmpty(sendMessageRequestBody.getCustomerExchange())){
            return new StringBuffer().append("customerExchange is paramter empty").toString();
        }else {
            // 自动序列化发送
            rabbitTemplate.convertAndSend(sendMessageRequestBody.getCustomerExchange(),sendMessageRequestBody.getRouteKey(),sendMessageRequestBody.getMessage());
        }
        return "OK";
    }

    @ApiOperation(value = "receive", notes = "接收信息", httpMethod = "GET")
    @GetMapping("receive/{queueName}")
    public Object receive(@PathVariable String queueName){
        // 接收后，数据被删除
        Object o = rabbitTemplate.receiveAndConvert(queueName);
        return o;
    }
}
