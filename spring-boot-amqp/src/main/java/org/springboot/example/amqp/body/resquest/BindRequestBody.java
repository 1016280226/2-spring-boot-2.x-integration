package org.springboot.example.example.amqp.body.resquest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springboot.example.example.amqp.constant.Exchange;
import org.springframework.amqp.core.Binding;

import javax.validation.constraints.NotBlank;
import java.util.Map;

import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "绑定-请求体")
public class BindRequestBody {

    @ApiModelProperty(value = "目标名称", example = "amqp.admin.queue", notes = "交换器| 队列 名称 ")
    @NotBlank(message = "目标名称必填")
    private String destination;

    @ApiModelProperty(value = "目的地类型", notes = "队列QUEUE 和 交换器EXCHANGE")
    private Binding.DestinationType destinationType = QUEUE ;

    @ApiModelProperty(value = "自定义的交换器", example = "amqp.admin.dirct")
    private String customerExchange;

    @ApiModelProperty(value = "默认的交换器")
    private Exchange defaultExchange;

    @ApiModelProperty(value = "路由键", example = "amqpAdmin")
    private String routingKey = null;

    @ApiModelProperty(value = "参数类型",example = "")
    private Map<String, Object> arguments = null;

}
