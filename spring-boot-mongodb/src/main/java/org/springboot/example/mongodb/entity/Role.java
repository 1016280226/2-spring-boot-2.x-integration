package org.springboot.example.mongodb.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author Calvin
 * @titile: Role POJO
 * @date 2019/2/28
 * @since 1.0
 */
@Data
@Document(collection = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = - 6843667995895038741L;

    private Long id;

    @Field("role_name")
    private String roleName = null;

    private String note = null;
}
