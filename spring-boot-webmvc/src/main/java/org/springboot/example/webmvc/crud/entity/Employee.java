package org.springboot.example.example.webmvc.crud.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employee")
@ApiModel(value = "员工")
public class Employee {

    @Id
	private Long id;

    @ApiModelProperty(value = "名字")
    @Field(value = "last_name")
    private String lastName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "出生年份")
    private Date birth;

    @ApiModelProperty(value = "部门")
    private Department department;

}
