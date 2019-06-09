package org.springboot.example.webmvc.crud.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "department")
public class Department {

	@Id
	private Long id;

	@ApiModelProperty(value = "部门名称")
	@Field(value = "department_name")
	private String departmentName;
}
