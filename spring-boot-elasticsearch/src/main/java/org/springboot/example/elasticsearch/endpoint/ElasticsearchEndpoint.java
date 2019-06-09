package org.springboot.example.elasticsearch.endpoint;

import io.swagger.annotations.Api;
import org.springboot.example.elasticsearch.entity.User;
import org.springboot.example.elasticsearch.respository.UserReposiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(value = "Elasticsearch",tags = {"Elasticsearch API"})
@RestController
@RequestMapping(value = "es/user")
public class ElasticsearchEndpoint {

    @Autowired
    private UserReposiory userReposiory;

    @PostMapping("create")
    public User create(@RequestBody User user) {
        return userReposiory.save(user);
    }

    @GetMapping("find/{id}")
    public Optional<User> findUser(@PathVariable String id) {
        return userReposiory.findById(id);
    }


}