package org.springboot.example.mongodb.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springboot.example.mongodb.entity.User;
import org.springboot.example.mongodb.service.UserService;
import org.springboot.example.starter.abstracts.constant.ApiUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Calvin
 * @titile: User Endpoint
 * @date 2019/2/28
 * @since 1.0
 */
@Api(value = "MongoDB",tags = {"MongoDB API"})
@RestController
@RequestMapping(ApiUrl.MONGODB + "/user")
@ApiModel
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "create", notes = "create", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("create")
    public User create(@RequestBody User user){
        userService.create(user);
        return user;
    }

    @ApiOperation(value = "update", notes = "update", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("update")
    public User update(@RequestBody User user){
        userService.update(user);
        return user;
    }

    @ApiOperation(value = "delete", notes = "delete", httpMethod = "GET", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("delete/{id}")
    public String update(@PathVariable Long id){
        userService.delete(User.builder().id(id).build());
        return "ok";
    }

    @ApiOperation(value = "query", notes = "query", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("query")
    public List<User> query(@RequestBody User user){
        List<User> users = userService.queryList(user);
        return users;
    }

    @ApiOperation(value = "get", notes = "get", httpMethod = "GET", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("get/{id}")
    public User getUser(@PathVariable Long id){
        User user = (User) userService.queryByPrimary(id);
        return user;
    }

}
