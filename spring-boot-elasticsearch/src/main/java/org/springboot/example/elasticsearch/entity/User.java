package org.springboot.example.elasticsearch.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


/**
 * @Auther: Calvin
 * @Date: 2019/6/4
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "user")
public class User {
    @Id
    private String id;

    @ApiModelProperty(value = "姓名", example = "Calvin")
    private String name;

    @ApiModelProperty(value = "性别", example = "0")
    private int sex;

    @ApiModelProperty(value = "年龄", example = "25")
    private int age;

}
