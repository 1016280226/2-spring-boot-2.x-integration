package org.springboot.example.example.mongodb.repository;

import org.springboot.example.example.mongodb.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Calvin
 * @titile: MongoDB + JPA
 * @date 2019/3/4
 * @since 1.0
 */
@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    /****** JPA 1.1 使用预定义查询 ******/
    List<User> findByUserName(String userName);

    /****** JPA 1.2 使用@Query 自定义查询 ******/
    // ?0 代表第一个参数
    // ?1 代表第二个参数
    @Query("{'id' : ?0, 'userName' : ?1}")
    User findByIdAndUserName(Long id, String userName);

    /****** JPA 1.3 实现自定义方法 -> UserRepositoryImpl ******/
    User findByIdOrUserName(Long id, String userName);

}
