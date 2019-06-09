package org.springboot.example.example.amqp.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springboot.example.example.amqp.body.resquest.BindRequestBody;
import org.springboot.example.example.amqp.body.resquest.CreateExchangeRequestBody;
import org.springboot.example.example.amqp.body.resquest.CreateQueueRequestBody;
import org.springboot.example.example.amqp.constant.Exchange;
import org.springboot.example.example.starter.abstracts.constant.ApiUrl;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Calvin
 * @titile: Amqp Admin Endpoint
 * @date 2019/3/27
 * @since 1.0
 */
@Slf4j
@Api(value = "RabbitMQ",tags = {"RabbitMQ Admin API"})
@RestController
@RequestMapping(ApiUrl.RABBITMQ + "/admin")
public class AmqpAdminEndpoint {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @ApiOperation(value = "create/exchange", notes = "创建交换器", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("create/exchange")
    public String createExchange(@RequestBody @Validated CreateExchangeRequestBody body){
      switch (body.getExchageType()){
          case direct:
              amqpAdmin.declareExchange(new DirectExchange(body.getName(),body.getDurable(),body.getAutoDelete(),body.getArguments()));
              break;
          case fanout:
              amqpAdmin.declareExchange(new FanoutExchange(body.getName(),body.getDurable(),body.getAutoDelete(),body.getArguments()));
              break;
          case topic:
              amqpAdmin.declareExchange(new TopicExchange(body.getName(),body.getDurable(),body.getAutoDelete(),body.getArguments()));
              break;
          case headers:
              amqpAdmin.declareExchange(new HeadersExchange(body.getName(),body.getDurable(),body.getAutoDelete(),body.getArguments()));
              break;
              default:
                  try {
                      throw new Exception("exchangeType is Paramter Error");
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
      }
        return "OK";
    }

    @ApiOperation(value = "delete/exchange", notes = "删除交换器", httpMethod = "DELETE", consumes = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping("delete/exchange/{name}")
    public String deleteExchange(@PathVariable(value = "name") String exchangeName){
        boolean b = amqpAdmin.deleteExchange(exchangeName);
        return b?"删除成功":"删除失败";
    }

    @ApiOperation(value = "create/queue", notes = "创建队列", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("create/queue")
    public String createQueue(@RequestBody @Validated CreateQueueRequestBody body){
        amqpAdmin.declareQueue(new Queue(body.getName(),body.getDurable(),body.getExclusive(),body.getAutoDelete(),body.getArguments()));
        return "OK";
    }

    @ApiOperation(value = "delete/queue", notes = "删除队列", httpMethod = "DELETE")
    @DeleteMapping("delete/queue/{name}")
    public String deleteQueue(@PathVariable(value = "name") String queueName){
        boolean b = amqpAdmin.deleteQueue(queueName);
        return b?"删除成功":"删除失败";
    }

    @ApiOperation(value = "bind", notes = "交换器和队列绑定", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("bind")
    public String bind(@RequestBody @Validated BindRequestBody body){
        if(body.getDefaultExchange() != null){
            if(body.getDefaultExchange() != Exchange.direct){
                return new StringBuffer().append(body.getDefaultExchange().name()).append("is paramter error").toString();
            }else {
                amqpAdmin.declareBinding(new Binding(body.getDestination(),body.getDestinationType(), body.getDefaultExchange().getMessage(), body.getRoutingKey(), body.getArguments()));
            }
        }else if(StringUtils.isEmpty(body.getCustomerExchange())){
            return new StringBuffer().append("customerExchange is paramter empty").toString();
        }else {
            amqpAdmin.declareBinding(new Binding(body.getDestination(),body.getDestinationType(), body.getCustomerExchange(), body.getRoutingKey(), body.getArguments()));
        }
        return "OK";
    }

    @ApiOperation(value = "unbind", notes = "解绑", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("unbind")
    public String unbind(@RequestBody @Validated BindRequestBody body){
        amqpAdmin.removeBinding(new Binding(body.getDestination(),body.getDestinationType(), body.getDefaultExchange().getMessage(), body.getRoutingKey(), body.getArguments()));
        return "OK";
    }




}
