package org.springboot.example.mongodb.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springboot.example.mongodb.entity.User;
import org.springboot.example.mongodb.repository.UserRepository;
import org.springboot.example.mongodb.utils.MongodbUtils;
import org.springboot.example.starter.abstracts.constant.ApiUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Calvin
 * @titile: User Endpoint
 * @date 2019/2/28
 * @since 1.0
 */
@Api(value = "MongoDB",tags = {"MongoDB JPA API"})
@RestController
@RequestMapping(ApiUrl.MONGODB_JPA + "/user")
@ApiModel
public class UserJpaEndpoint {

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "create", notes = "create", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("create")
    public User create(@RequestBody User user){
        userRepository.insert(user);
        return user;
    }

    @ApiOperation(value = "update", notes = "update", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("update")
    public User update(@RequestBody User user){
        User save = userRepository.save(user);
        return save;
    }

    @ApiOperation(value = "delete", notes = "delete", httpMethod = "GET", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("delete/{id}")
    public String update(@PathVariable Long id){
        userRepository.delete(User.builder().id(id).build());
        return "ok";
    }

    @ApiOperation(value = "queryPage", notes = "queryPage", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("queryPage")
    public Page<User> queryPage(@RequestBody User user, Pageable pageable){
//        Page exampleSort = new Sort(Sort.Direction.DESC,"id");
//        Pageable examplePageable = new PageRequest(page-1,size,sort);
        Page<User> usersPage = userRepository.findAll(MongodbUtils.exampleByObject(user), pageable);
        return usersPage;
    }

    @ApiOperation(value = "get", notes = "get", httpMethod = "GET", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("get/{id}")
    public User getUser(@PathVariable Long id){
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.get();
        return user;
    }

}
