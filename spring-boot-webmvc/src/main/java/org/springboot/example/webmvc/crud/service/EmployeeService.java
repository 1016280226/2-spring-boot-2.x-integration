package org.springboot.example.example.webmvc.crud.service;

import org.springboot.example.example.starter.abstracts.crud.CRUD;
import org.springboot.example.example.webmvc.crud.entity.Employee;

import java.util.List;

/**
 * @author Calvin
 * @titile:
 * @date 2019/2/28
 * @since 1.0
 */
public interface EmployeeService extends CRUD<Employee> {

    List<Employee> queryAll();
}
