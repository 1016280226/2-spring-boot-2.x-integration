package org.springboot.example.elasticsearch.respository;

import org.springboot.example.elasticsearch.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserReposiory extends CrudRepository<User, String> {

}