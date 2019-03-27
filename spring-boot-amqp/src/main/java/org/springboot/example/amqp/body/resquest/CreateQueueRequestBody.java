package org.springboot.example.amqp.body.resquest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springboot.example.amqp.constant.Exchange;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "队列创建-请求体")
public class CreateQueueRequestBody {

    @ApiModelProperty(value = "队列名称",example = "amqp.admin.queue")
    @NotBlank(message = "队列名称必填")
    private String name;

    @ApiModelProperty(value = "是否持久化",example = "true")
    private Boolean durable = Boolean.TRUE;

    @ApiModelProperty(value = "是否互斥",example = "false")
    private Boolean exclusive = Boolean.FALSE;

    @ApiModelProperty(value = "是否自动删除",example = "false")
    private Boolean autoDelete = Boolean.FALSE;

    @ApiModelProperty(value = "参数类型",example = "")
    private Map<String, Object> arguments = null;

}
