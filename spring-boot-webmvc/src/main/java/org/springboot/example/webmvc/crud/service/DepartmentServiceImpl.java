package org.springboot.example.example.webmvc.crud.service;


import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springboot.example.example.mongodb.utils.MongodbUtils;
import org.springboot.example.example.webmvc.crud.entity.Department;
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
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void create(Department department) {
        // 添加
        mongoTemplate.insert(department);
    }

    @Override
    public void delete(Department department) {
        // 方法一
        mongoTemplate.findAndRemove(MongodbUtils.queryByCondition(department), department.getClass());

        // 方法二
        DeleteResult remove = mongoTemplate.remove(MongodbUtils.queryByCondition(department), Department.class);
        System.out.println(remove);
    }

    @Override
    public void update(Department department) {
        Query query = MongodbUtils.queryByCondition(Department.builder().id(department.getId()).build());
        Update update = MongodbUtils.updateByCondition(department);
        // 更新多个对象
        UpdateResult updateResult = mongoTemplate.updateMulti(
                query,
                update,
                Department.class
        );
        System.out.println(updateResult);
    }

    @Override
    public Department queryOne(Department department) {
        Department one = (Department) mongoTemplate.findOne(MongodbUtils.queryByCondition(department), Department.class);
        return one;
    }

    @Override
    public Department queryByPrimary(Long id) {
        // 第一种方法: 通过对象
        return mongoTemplate.findById(id, Department.class);

        // 第二种方法: 通过 Criteria 条件
//        Criteria criteriaId = Criteria.where("id").is(id);
//        Query queryId = Query.query(criteriaId);
//        Department department = mongoTdepartmentlate.findOne(queryId, Department.class);
//        return department;
    }

    @Override
    public List queryList(Department department) {
        List<?> departments = mongoTemplate.find(MongodbUtils.queryByCondition(department), Department.class);
        return departments;
    }

    @Override
    public List<Department> queryAll() {
        List<Department> all = mongoTemplate.findAll(Department.class);
        return all;
    }
}
