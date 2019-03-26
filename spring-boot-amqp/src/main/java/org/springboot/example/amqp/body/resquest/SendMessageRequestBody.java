package org.springboot.example.amqp.body.resquest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springboot.example.amqp.constant.Exchage;
import springfox.documentation.service.ApiListing;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "发送消息-请求体")
public class SendMessageRequestBody {

    @ApiModelProperty(value = "默认交换器",example = "0", notes = "1.用来接收生产者发送的消息，并将这些消息路由给服务器中的队列;")
    private Exchage defaultExchage;

    @ApiModelProperty(value = "自定义交换器",example = "", notes = "1.用来接收生产者发送的消息，并将这些消息路由给服务器中的队列;")
    private String customerExchage;

    @ApiModelProperty(value ="路由键",example = "amq.direct",notes = "该路由键存唯一标识")
    @NotBlank(message = "路由键参数必填")
    private String routeKey;

    @ApiModelProperty(value = "消息体",example = "RabbitMQ 第一个消息",notes = "默认当成消息体")
    @NotNull(message = "消息体必填")
    private Object message;

}
