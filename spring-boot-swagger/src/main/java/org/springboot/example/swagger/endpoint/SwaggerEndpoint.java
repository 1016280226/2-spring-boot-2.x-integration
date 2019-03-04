package org.springboot.example.swagger.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springboot.example.swagger.body.SwaggerCreateRequestBody;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Calvin on 2018/4/6
 */
@Api(value = "Swagger",tags = {"Swagger API"})
@RestController
@RequestMapping("swagger")
public class SwaggerEndpoint {

    @ApiOperation(value = "create", notes = "create", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "create", produces ={MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}) //produces：指定处理请求返回值内容类型  consumes：指定处理请求的提交内容类型（Content-Type）
    public SwaggerCreateRequestBody create(@RequestBody SwaggerCreateRequestBody body){
        return body;
    }
}
