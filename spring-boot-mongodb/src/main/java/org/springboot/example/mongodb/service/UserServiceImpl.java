package org.springboot.example.mongodb.service;


import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springboot.example.example.mongodb.utils.MongodbUtils;
import org.springboot.example.mongodb.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Calvin
 * @titile:
 * @date 2019/2/28
 * @since 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void create(User user) {
        // 添加
        mongoTemplate.insert(user);
    }

    @Override
    public void delete(User user) {
        // 方法一
        mongoTemplate.findAndRemove(MongodbUtils.queryByCondition(user), user.getClass());

        // 方法二
        DeleteResult remove = mongoTemplate.remove(MongodbUtils.queryByCondition(user), User.class);
        System.out.println(remove);
    }

    @Override
    public void update(User user) {
        Query query = MongodbUtils.queryByCondition(User.builder().id(user.getId()).build());
        Update update = MongodbUtils.updateByCondition(user);
        // 更新多个对象
        UpdateResult updateResult = mongoTemplate.updateMulti(
                query,
                update,
                User.class
        );
        System.out.println(updateResult);
    }

    @Override
    public User queryOne(User user) {
        User one = (User) mongoTemplate.findOne(MongodbUtils.queryByCondition(user), User.class);
        return one;
    }

    @Override
    public User queryByPrimary(Long id) {
        // 第一种方法: 通过对象
        return mongoTemplate.findById(id, User.class);

        // 第二种方法: 通过 Criteria 条件
//        Criteria criteriaId = Criteria.where("id").is(id);
//        Query queryId = Query.query(criteriaId);
//        User user = mongoTemplate.findOne(queryId, User.class);
//        return user;
    }

    @Override
    public List queryList(User user) {
        List<?> users = mongoTemplate.find(MongodbUtils.queryByCondition(user), User.class);
        return users;
    }


}
