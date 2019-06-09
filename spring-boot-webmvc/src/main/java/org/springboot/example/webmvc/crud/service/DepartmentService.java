package org.springboot.example.webmvc.crud.service;

import org.springboot.example.starter.abstracts.crud.CRUD;
import org.springboot.example.webmvc.crud.entity.Department;

import java.util.List;

/**
 * @author Calvin
 * @titile:
 * @date 2019/2/28
 * @since 1.0
 */
public interface DepartmentService extends CRUD<Department> {

    List<Department> queryAll();
}
