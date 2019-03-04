package org.springboot.example.mongodb.utils;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author Calvin
 * @titile:
 * @date 2019/2/28
 * @since 1.0
 */
public class MongodbUtils<T>{

    /**
     * 通过对象条件查询
     *
     * @param t
     * @param <T>
     * @return
     * @criteria
     * 1.is 等于
     * 2.regex 模糊查询
     * 3.and 和
     * 4.or  或
     */
    public static <T> Query queryByCondition(T t){
        BeanMap beanMap = BeanMap.create(t);
        // mongodb 属性条件查询
        Criteria criteria  = null;
        int i = 1;
        for(Object key : beanMap.keySet()){
            if(null != beanMap.get(key)){
                if(i == 1 && key.toString().equals("id")){
                    criteria = Criteria.where(key.toString()).is(beanMap.get(key));
                    i ++;
                }else if(i == 1 && !key.toString().equals("id")){
                    criteria = Criteria.where(key.toString()).is(beanMap.get(key));
                    i ++;
                }else {
                    criteria = criteria.and(key.toString()).is(beanMap.get(key));
                }
            }
        }
        Query query = Query.query(criteria);
        return query;
    }


    /**
     * 通过对象条件更新
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Update updateByCondition(T t){
        BeanMap beanMap = BeanMap.create(t);
        // mongodb 属性条件查询
        Update update  = null;
        int i = 1;
        for(Object key : beanMap.keySet()){
            if(null != beanMap.get(key) && !key.toString().equals("id")){
                if(i == 1){
                    update = Update.update(key.toString(), beanMap.get(key));
                    i ++;
                }else {
                    update = update.set(key.toString(), beanMap.get(key));
                }
            }
        }
        return update;
    }

    /**
     * 通过对象Example 进行查询
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Example exampleByObject(T t){
        BeanMap beanMap = BeanMap.create(t);
        ExampleMatcher matcher  = ExampleMatcher.matching();
        for(Object key : beanMap.keySet()) {
            if (null != beanMap.get(key)) {
                matcher.withMatcher(key.toString(), ExampleMatcher.GenericPropertyMatchers.contains());
            }
        }
        Example example  = Example.of(beanMap,matcher);
        return example;
    }
}
