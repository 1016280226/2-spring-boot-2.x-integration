package org.springboot.example.webmvc.crud.service;


import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springboot.example.example.mongodb.utils.MongodbUtils;
import org.springboot.example.webmvc.crud.entity.Employee;
import org.springboot.example.webmvc.crud.service.EmployeeService;
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
public class EmployeeServiceImpl implements EmployeeService {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void create(Employee emp) {
        // 添加
        mongoTemplate.insert(emp);
    }

    @Override
    public void delete(Employee emp) {
        // 方法一
        mongoTemplate.findAndRemove(MongodbUtils.queryByCondition(emp), emp.getClass());

        // 方法二
        DeleteResult remove = mongoTemplate.remove(MongodbUtils.queryByCondition(emp), Employee.class);
        System.out.println(remove);
    }

    @Override
    public void update(Employee emp) {
        Query query = MongodbUtils.queryByCondition(Employee.builder().id(emp.getId()).build());
        Update update = MongodbUtils.updateByCondition(emp);
        // 更新多个对象
        UpdateResult updateResult = mongoTemplate.updateMulti(
                query,
                update,
                Employee.class
        );
        System.out.println(updateResult);
    }

    @Override
    public Employee queryOne(Employee emp) {
        Employee one = (Employee) mongoTemplate.findOne(MongodbUtils.queryByCondition(emp), Employee.class);
        return one;
    }

    @Override
    public Employee queryByPrimary(Long id) {
        // 第一种方法: 通过对象
        return mongoTemplate.findById(id, Employee.class);

        // 第二种方法: 通过 Criteria 条件
//        Criteria criteriaId = Criteria.where("id").is(id);
//        Query queryId = Query.query(criteriaId);
//        Employee emp = mongoTemplate.findOne(queryId, Employee.class);
//        return emp;
    }

    @Override
    public List queryList(Employee emp) {
        List<?> emps = mongoTemplate.find(MongodbUtils.queryByCondition(emp), Employee.class);
        return emps;
    }

    @Override
    public List<Employee> queryAll() {
        List<Employee> all = mongoTemplate.findAll(Employee.class);
        return all;
    }
}
