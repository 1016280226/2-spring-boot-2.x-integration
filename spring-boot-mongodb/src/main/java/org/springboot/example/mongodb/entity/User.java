package org.springboot.example.mongodb.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

/**
 * @author Calvin
 * @titile: User POJO
 * @date 2019/2/28
 * @annotations
 * 1.@Document 当作文档来使用
 * 2.@Field    做字段之间命名规则的转换
 * @since 1.0
 */
@Data
@Builder
@ApiModel(value = "User")
@Document(collection = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -6843667995895038741L;

    @Id
    @ApiModelProperty(value = "id")
    private Long id;

    @Field("user_name") // 在 MongoDB 中使用 user_name 保存属性
    @ApiModelProperty(value = "用户名")
    private String userName = null;

    @ApiModelProperty(value = "备注")
    private String note = null;

    @ApiModelProperty(value = "角色")
    private List<Role> roles = null;
}
