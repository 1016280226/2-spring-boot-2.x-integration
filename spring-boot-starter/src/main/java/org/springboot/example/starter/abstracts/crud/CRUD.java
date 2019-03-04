package org.springboot.example.starter.abstracts.crud;

import java.util.List;

/**
 * @author Calvin
 * @titile:
 * @date 2019/2/28
 * @since 1.0
 */
public abstract interface CRUD<T>{
    void create(T t);
    void delete(T t);
    void update(T t);
    T queryOne(T t);
    T queryByPrimary(Long id);
    List<T> queryList(T t);

}
