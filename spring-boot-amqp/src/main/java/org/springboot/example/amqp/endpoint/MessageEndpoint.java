package org.springboot.example.amqp.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springboot.example.amqp.body.resquest.SendMessageRequestBody;
import org.springboot.example.amqp.constant.Exchage;
import org.springboot.example.starter.abstracts.constant.ApiUrl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "direct", notes = "单播(点对点)", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("send/direct")
    public String sendDirect(@RequestBody @Validated SendMessageRequestBody sendMessageRequestBody){
        if(sendMessageRequestBody.getDefaultExchage() != null){
            if(sendMessageRequestBody.getDefaultExchage() != Exchage.direct){
                return new StringBuffer().append(sendMessageRequestBody.getDefaultExchage().name()).append("is paramter error").toString();
            }else {
                // 自动序列化发送
                rabbitTemplate.convertAndSend(sendMessageRequestBody.getDefaultExchage().getMessage(),sendMessageRequestBody.getRouteKey(),sendMessageRequestBody.getMessage());
            }
        }else if(StringUtils.isEmpty(sendMessageRequestBody.getCustomerExchage())){
            return new StringBuffer().append("customerExchage is paramter empty").toString();
        }else {
            // 自动序列化发送
            rabbitTemplate.convertAndSend(sendMessageRequestBody.getCustomerExchage(),sendMessageRequestBody.getRouteKey(),sendMessageRequestBody.getMessage());
        }
        return "OK";
    }
}
