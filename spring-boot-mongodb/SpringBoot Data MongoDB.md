# 笔记一 SpringBoot Data MongoDB 

[TOC]

## 1. MongoDB 简介

### 1.1 什么是mongoDB ?

> - MongoDB 是由**C++**语言编写的，是一个基于**分布式文件存储**的开源数据库系统。
> - 在高负载的情况下，添加更多的节点，可以保证服务器性能。
> - MongoDB 旨在为**WEB应用**提供可扩展的高性能数据存储解决方案。
> - MongoDB 将数据**存储为一个文档**，数据结构由**键值(key=>value)**对组成。
> - MongoDB 文档类似于 **JSON 对象**。字段值可以包含其他文档，数组及文档数组。



### 1.2 主要特性

> - MongoDB 是一个**面向文档存储的数据库**，操作起来比较简单和容易。
> - 你可以在MongoDB记录中**设置任何属性的索引** (如：FirstName="Sameer",Address="8 Gandhi Road")来实现更快的排序。
> - 你可以通过**本地或者网络创建数据镜像**，这使得MongoDB有更强的扩展性。
> - 如果负载的增加（需要更多的存储空间和更强的处理能力） ，它可以**分布在计算机网络中的其他节点**上这就是所谓的分片。
> - Mongo支持丰富的**查询表达式**。查询指令使用JSON形式的标记，可轻易查询文档中内嵌的对象及数组。
> - MongoDb 使用update()命令可以**实现替换完成的文档（数据）**或者一些**指定的数据字段** 。
> - Mongodb中的**Map/reduce**主要是用来对数据进行**批量处理**和**聚合操作**。
> - Map和Reduce。**Map函数调用emit(key,value)**遍历集合中所有的记录，将key与value传给Reduce函数进行处理。
> - Map函数和Reduce函数是使用Javascript编写的，并可以通过db.runCommand或mapreduce命令来执行MapReduce操作。
> - **GridFS**是MongoDB中的一个内置功能，可以用于**存放大量小文件。**
> - MongoDB**允许在服务端执行脚本**，可以用Javascript编写某个函数，直接在服务端执行，也可以把函数的定义存储在服务端，下次直接调用即可。
> - MongoDB**支持各种编程语言**:RUBY，PYTHON，JAVA，C++，PHP，C#等多种语言。
> - MongoDB安装简单。



## 2. 安装 MongoDB

### 2.1 Windows 平台下载安装 MongoDB

MongoDB 提供了可用于 32 位和 64 位系统的预编译二进制包，你可以从MongoDB官网下载安装，MongoDB 预编译二进制包下载地址：<https://www.mongodb.com/download-center#community>

> 注意：在 MongoDB 2.2 版本后已经不再支持 Windows XP 系统。最新版本也已经没有了 32 位系统的安装文件。

![img](http://www.runoob.com/wp-content/uploads/2013/10/mongodb-download-1.jpg)



- **MongoDB for Windows 64-bit** 适合 64 位的 Windows Server 2008 R2, Windows 7 , 及最新版本的 Window 系统。
- **MongoDB for Windows 32-bit** 适合 32 位的 Window 系统及最新的 Windows Vista。 32 位系统上 MongoDB 的数据库最大为 2GB。
- **MongoDB for Windows 64-bit Legacy** 适合 64 位的 Windows Vista, Windows Server 2003, 及 Windows Server 2008 

 根据你的系统下载 32 位或 64 位的 .msi 文件，下载后双击该文件，按操作提示安装即可。

安装过程中，你可以通过点击 "Custom(自定义)" 按钮来设置你的安装目录。

![img](http://www.runoob.com/wp-content/uploads/2013/10/win-install1.jpg)



![img](http://www.runoob.com/wp-content/uploads/2013/10/win-install2.jpg)

下一步安装 **"install mongoDB compass"** 不勾选，否则可能要很长时间都一直在执行安装，MongoDB Compass 是一个图形界面管理工具，我们可以在后面自己到官网下载安装，下载地址：<https://www.mongodb.com/download-center/compass>。

![img](http://www.runoob.com/wp-content/uploads/2013/10/8F7AF133-BE49-4BAB-9F93-88A9D666F6C0.jpg)



#### 2.12 创建数据目录

MongoDB将数据目录存储在 db 目录下。但是这个数据目录不会主动创建，我们在安装完成后需要创建它。请注意，数据目录应该放在根目录下（(如： C:\ 或者 D:\ 等 )。

在本教程中，我们已经在 C 盘安装了 mongodb，现在让我们创建一个 data 的目录然后在 data 目录里创建 db 目录。

```
c:\>cd c:\

c:\>mkdir data

c:\>cd data

c:\data>mkdir db

c:\data>cd db

c:\data\db>
```

你也可以通过 window 的资源管理器中创建这些目录，而不一定通过命令行。



如果执行成功，会输出如下信息：

```
2015-09-25T15:54:09.212+0800 I CONTROL  Hotfix KB2731284 or later update is not
installed, will zero-out data files
2015-09-25T15:54:09.229+0800 I JOURNAL  [initandlisten] journal dir=c:\data\db\j
ournal
2015-09-25T15:54:09.237+0800 I JOURNAL  [initandlisten] recover : no journal fil
es present, no recovery needed
2015-09-25T15:54:09.290+0800 I JOURNAL  [durability] Durability thread started
2015-09-25T15:54:09.294+0800 I CONTROL  [initandlisten] MongoDB starting : pid=2
488 port=27017 dbpath=c:\data\db 64-bit host=WIN-1VONBJOCE88
2015-09-25T15:54:09.296+0800 I CONTROL  [initandlisten] targetMinOS: Windows 7/W
indows Server 2008 R2
2015-09-25T15:54:09.298+0800 I CONTROL  [initandlisten] db version v3.0.6
……
```



#### 2.1.3 连接MongoDB

我们可以在命令窗口中运行 mongo.exe 命令即可连接上 MongoDB，执行如下命令：

```
C:\mongodb\bin\mongo.exe
```

------



#### 2.1.4 配置 MongoDB 服务

**管理员模式打开命令行窗口**

创建目录，执行下面的语句来创建数据库和日志文件的目录

```
mkdir c:\data\db
mkdir c:\data\log
```

**创建配置文件**

创建一个配置文件。该文件必须设置 systemLog.path 参数，包括一些附加的配置选项更好。

例如，创建一个配置文件位于 C:\mongodb\mongod.cfg，其中指定 systemLog.path 和 storage.dbPath。具体配置内容如下：

```
systemLog:
    destination: file
    path: c:\data\log\mongod.log
storage:
    dbPath: c:\data\db
```



#### 2.1.5 安装 MongoDB服务

通过执行mongod.exe，使用--install选项来安装服务，使用--config选项来指定之前创建的配置文件。

```
C:\mongodb\bin\mongod.exe --config "C:\mongodb\mongod.cfg" --install
```

要使用备用 dbpath，可以在配置文件（例如：C:\mongodb\mongod.cfg）或命令行中通过 --dbpath 选项指定。

如果需要，您可以安装 mongod.exe 或 mongos.exe 的多个实例的服务。只需要通过使用 --serviceName 和 --serviceDisplayName 指定不同的实例名。只有当存在足够的系统资源和系统的设计需要这么做。

启动MongoDB服务

```
net start MongoDB
```

关闭MongoDB服务

```
net stop MongoDB
```

移除 MongoDB 服务

```
C:\mongodb\bin\mongod.exe --remove
```

> **命令行下运行 MongoDB 服务器** 和 **配置 MongoDB 服务** 任选一个方式启动就可以。



### 2.2  Linux平台下载安装MongoDB

MongoDB 提供了 linux 各发行版本 64 位的安装包，你可以在官网下载安装包。

下载地址：<https://www.mongodb.com/download-center#community>

下载完安装包，并解压 **tgz**（以下演示的是 64 位 Linux上的安装） 。

下载完安装包，并解压 **tgz**（以下演示的是 64 位 Linux上的安装） 。

```
curl -O https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-3.0.6.tgz    # 下载
tar -zxvf mongodb-linux-x86_64-3.0.6.tgz                                   # 解压

mv  mongodb-linux-x86_64-3.0.6/ /usr/local/mongodb                         # 将解压包拷贝到指定目录
```

MongoDB 的可执行文件位于 bin 目录下，所以可以将其添加到 **PATH** 路径中：

```
export PATH=<mongodb-install-directory>/bin:$PATH
```

**<mongodb-install-directory>** 为你 MongoDB 的安装路径。如本文的 **/usr/local/mongodb** 。



#### 2.2.1 创建数据库目录

MongoDB的数据存储在data目录的db目录下，但是这个目录在安装过程不会自动创建，所以你需要手动创建data目录，并在data目录中创建db目录。

以下实例中我们将data目录创建于根目录下(/)。

注意：/data/db 是 MongoDB 默认的启动的数据库路径(--dbpath)。

```
mkdir -p /data/db
```



#### 2.2.2 命令行中运行 MongoDB 服务

你可以再命令行中执行mongo安装目录中的bin目录执行mongod命令来启动mongdb服务。

> 注意：如果你的数据库目录不是/data/db，可以通过 --dbpath 来指定。

```
$ ./mongod
2015-09-25T16:39:50.549+0800 I JOURNAL  [initandlisten] journal dir=/data/db/journal
2015-09-25T16:39:50.550+0800 I JOURNAL  [initandlisten] recover : no journal files present, no recovery needed
2015-09-25T16:39:50.869+0800 I JOURNAL  [initandlisten] preallocateIsFaster=true 3.16
2015-09-25T16:39:51.206+0800 I JOURNAL  [initandlisten] preallocateIsFaster=true 3.52
2015-09-25T16:39:52.775+0800 I JOURNAL  [initandlisten] preallocateIsFaster=true 7.7
```



## 3. Maven 依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-boot</artifactId>
        <groupId>org.springboot.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-boot-mongodb</artifactId>

    <properties>
        <springboot>2.1.3.RELEASE</springboot>
        <project>1.0-SNAPSHOT</project>
    </properties>

    <dependencies>

        <!-- springboot starter -->
        <dependency>
            <groupId>org.springboot.example</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <version>${project}</version>
        </dependency>

        <!-- springboot commands dependencies -->
        <dependency>
            <groupId>org.springboot.example</groupId>
            <artifactId>spring-boot-commonds-dependencies</artifactId>
            <version>${project}</version>
        </dependency>

        <!-- springboot commands abstracts -->
        <dependency>
            <groupId>org.springboot.example</groupId>
            <artifactId>spring-boot-commonds-abstracts</artifactId>
            <version>${project}</version>
        </dependency>

        <!-- springboot mongodb -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
            <version>${springboot}</version>
        </dependency>

        <!-- spirngboot swagger -->
        <dependency>
            <groupId>org.springboot.example</groupId>
            <artifactId>spring-boot-swagger</artifactId>
            <version>${project}</version>
        </dependency>

    </dependencies>
</project>
```



## 4. MongoDB 配置

spring-boot-mongodb 项目下的 resources 下application-dev.yml配置文件添加如下配置

```yaml
server:
  port: 8080

 ###################### mongodb #########################
spring:
  data:
    mongodb:
      authentication-database:           # 用于签名的MongoDB 数据库
      database: mongodb_1                # 数据库名称
      field-naming-strategy:             # 使用字段的策略
      grid-fs-database:                  # GridFs （网格文件） 数据库名称
      host: 127.0.0.1                    # MongoDB 服务器，不能设置URI
      password:                          # MongoDB 服务器用户密码，不能设置URI
      port: 27017                        # MongoDB 服务器端口，不能设置URI
      repositories:
        type: auto                       # 是否启用 Mon go DB 关于 JPA 规范的编程
      uri: mongodb://127.0.0.1/mongodb_1 # MongoDB 默认 URI
      username:                          # MongoDB 服务器用户名，不能设置为 URI
```



## 5. MongoDB 使用

### 5.1 创建POJO

> @Document  该标志记录为当前为MongoDB 文档来使用，使用在类上
>
> @Field             做字段之间命名规则的转换

1.标记为用户文档

```java
package org.springboot.example.mongodb.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

/**
 * @author Calvin
 * @titile: User POJO
 * @date 2019/2/28
 * @annotations 
 * 1.@Document 当作文档来使用
 * 2.@Field    做字段之间命名规则的转换
 * @since 1.0
 */
@Data
@Builder
@ApiModel(value = "User")
@Document(collection = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -6843667995895038741L;

    @Id
    @ApiModelProperty(value = "id")
    private Long id;

    @Field("user_name") // 在 MongoDB 中使用 user_name 保存属性
    @ApiModelProperty(value = "用户名")
    private String userName = null;

    @ApiModelProperty(value = "备注")
    private String note = null;

    @ApiModelProperty(value = "角色")
    private List<Role> roles = null;
}

```

2. 标识为角色文档

```java
package org.springboot.example.mongodb.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author Calvin
 * @titile: Role POJO
 * @date 2019/2/28
 * @since 1.0
 */
@Data
@Document(collection = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = - 6843667995895038741L;

    private Long id;

    @Field("role_name")
    private String roleName = null;

    private String note = null;
}

```



### 5.2 使用 MongoTemplate CRUD实例

```java
package org.springboot.example.mongodb.service;


import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springboot.example.mongodb.entity.User;
import org.springboot.example.mongodb.utils.MongodbUtils;
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
```



```java
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
```



### 5.3 使用MongoDB + JPA 

#### 5.3.1 使用预定义查询

```java
package org.springboot.example.mongodb.repository;

import org.springboot.example.mongodb.entity.User;
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
}

```



#### 5.3.2 使用@Query 自定义查询

```java
package org.springboot.example.mongodb.repository;

import org.springboot.example.mongodb.entity.User;
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

    /****** JPA 1.2 使用@Query 自定义查询 ******/
    // ?0 代表第一个参数
    // ?1 代表第二个参数
    @Query("{'id' : ?0, 'userName' : ?1}")
    User findByIdAndUserName(Long id, String userName);

}
```



#### 5.3.3 使用实现自定义方法

```java
package org.springboot.example.mongodb.repository;

import org.springboot.example.mongodb.entity.User;
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
  
    /****** JPA 1.3 实现自定义方法 -> UserRepositoryImpl ******/
    User findByIdOrUserName(Long id, String userName);

}
```

创建UserRepositoryImpl

```java
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
```



#### 5.3.4 启动配置 @EnableMongoRepositories

```java
package org.springboot.example.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Calvin
 * @titile: @EnableMongoRepositories
 * @date 2019/2/28
 * @since 1.0
 * @annonation
 *   1. @EnableMongoRepositories : 指定扫描的包，用于扫描继承了MongoRepository 的接口
 *      a.basePackages: 扫描包
 *      b.repositoryImplementationPostfix: 实现 Repository 接口，自定义类的后缀名称。默认impl
 */
@EnableSwagger2
@EnableSpringDataWebSupport
@EnableMongoRepositories(
        basePackages = "org.springboot.example.mongodb.repository",
        repositoryImplementationPostfix = "impl"
)
@SpringBootApplication(
        scanBasePackages = "org.springboot.example.mongodb")
public class MongoDBSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoDBSpringBootApplication.class, args);
    }
}
```