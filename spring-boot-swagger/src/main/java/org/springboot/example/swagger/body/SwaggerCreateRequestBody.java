package org.springboot.example.example.swagger.body;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Swagger Create")
public class SwaggerCreateRequestBody {

    @ApiModelProperty(value = "swagger Paramter1", example = "Hello! Swagger")
    @NotBlank
    private String swaggerParamter1;

    @ApiModelProperty(value = "swagger Paramter2", example = "Hello! Resful API")
    @NotBlank
    private String swaggerParamter2;
}


