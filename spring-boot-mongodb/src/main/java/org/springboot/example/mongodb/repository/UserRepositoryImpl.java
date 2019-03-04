package org.springboot.example.mongodb.repository;

import org.springboot.example.mongodb.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Calvin
 * @titile: 实现自定义方法
 * @date 2019/3/4
 * @since 1.0
 * @mind 注意这里类名称，默认要求是接口名称（UserRepository) +”impl" 这里 Spring JPA 会自动找到这个类作为接口方法实现
 */
@Repository
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public User findUserByIdOrUserName(Long id, String userName){
        Criteria criteriaId = Criteria.where("id").is(id);
        Criteria criteriaUserName = Criteria.where("userName").is(userName);
        Criteria criteria = new Criteria();
        criteria.orOperator(criteriaId, criteriaUserName);
        Query query = Query.query(criteria);
        return mongoTemplate.findOne(query,User.class);
    }

}
