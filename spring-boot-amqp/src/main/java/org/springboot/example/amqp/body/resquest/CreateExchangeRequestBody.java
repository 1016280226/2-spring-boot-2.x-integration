package org.springboot.example.example.amqp.body.resquest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springboot.example.example.amqp.constant.Exchange;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "交换器创建-请求体")
public class CreateExchangeRequestBody {

    @ApiModelProperty(value = "交换器类型",example = "0")
    @NotNull(message = "交换器类型必填")
    private Exchange exchageType;

    @ApiModelProperty(value = "交换器名称",example = "amqp.admin.direct")
    private String name;

    @ApiModelProperty(value = "是否持久化",example = "true")
    private Boolean durable;

    @ApiModelProperty(value = "是否自动删除",example = "false")
    private Boolean autoDelete;

    @ApiModelProperty(value = "参数类型",example = "0")
    private Map<String, Object> arguments = null;

}
