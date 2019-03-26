# 笔记三 SpringBoot AMQP

[TOC]

## 1.AMQP 简介

> 1. 什么 是 AMQP
>
> - **Advence Message Queue** **Protocol**   高级消息队列
> - 高级消息队列协议，也是一个**消息代理的规范**，兼容**JMS **
> - RabbitMQ是AMQP的实现
>
> 2. 作用:
>    - 可以通过消息中间件来**提升异步通信**、**拓展解耦**能力



## 2. 核心概念

### 2.1 Message 

> **消息**
>
> - **消息**是不具名的，它由**消息头**和**消息体**组成。
> - 消息体是不透明的，而消息头则由一系列的可选属性组 成，这些属性包括
>   - **routing-key（路由键**）、
>   - **priority（相对于其他消息的优先权）**、
>   - **delivery-mode（指出 该消息可能需要持久性存储）**等。

### 2.2 Publisher

>  消息的**生产者**，也是一个向**交换器**发布消息的客户端应用程序。

### 2.3 Exchange 

> **交换器**
>
> -   作用：用来**接收生产者发送的消息**，并将这些**消息路由给服务器中的队列**。
>
> -  Exchange有4种类型：
>
>   - **direct(默认)**，
>   - **fanout**,
>   -  **topic**
>   - **headers**
>
>   不同类型的Exchange转发消息的策略有 所区别

### 2.4 Queue 

>  **消息队列**
>
> - 作用：用来**保存消息**直到发送给消费者。它是消息的容器，也是消息的终点。
> - 一个消息 可**投入一个**或**多个**队列。消息一直在队列里面，等待消费者连接到这个队列将其取走。

### 2.5 Binding

>  **绑定**
>
> - 作用：用于**消息队列和交换器**之间的关联。
> - 一个绑定就是基于**路由键** -> 将**交换器**和**消息队列连** 接起来的**路由规则**，所以可以将交换器理解成一个由绑定构成的路由表。 
> - **Exchange** 和**Queue**的绑定可以是**多对多的关系**。

### 2.6 Connection 

>  网络连接，比如一个TCP连接。

### 2.7 Channel 

> 信道
>
> - 含义：**多路**复用连接中的一条独立的**双向数据流通道**。
> - 信道是建立在真实的TCP连接内的虚 拟连接，AMQP 命令都是通过信道发出去的，不管是发布消息、订阅队列还是接收消息，这 些动作都是通过信道完成。因为对于操作系统来说建立和销毁 TCP 都是非常昂贵的开销，所 以引入了信道的概念，以复用一条 TCP 连接。

### 2.8 Consumer 

> **消息的消费者**
>
> - 含义：表示一个从**消息队列中取出消息**的客户端应用程序。

### 2.9 Virtual Host

>  虚拟主机
>
> -  含义：表示一批交换器、消息队列和相关对象。
> - 虚拟主机是共享相同的身份认证和加 密环境的独立服务器域。每个 vhost 本质上就是一个 mini 版的 RabbitMQ 服务器，拥有 自己的队列、交换器、绑定和权限机制。vhost 是 AMQP 概念的基础，必须在连接时指定， RabbitMQ 默认的 vhost 是 / 。 

### 2.10 Broker 

> 表示消息队列服务器实体



## 3. 运行原理

> 绑定运行流程

![1552900952467](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1552900952467.png)



> 点对点模式 direct

![1552901088344](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1552901088344.png)

> 广播模式 fanout 

![1552901132961](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1552901132961.png)

> 条件规则路由模式  topic

![1552901191537](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1552901191537.png)

## 4. [Windows安装和下载](https://www.rabbitmq.com/install-windows-manual.html)

###  4.1 下载安装 Erlang/OTP

- **安装 Erlang / OTP**

  > 下载地址：<http://www.erlang.org/downloads>

- **设置ERLANG_HOME环境变量**

  如果现有RabbitMQ安装，并且代理作为服务运行，并且您安装了具有不同体系结构的Erlang VM，则必须在更新ERLANG_HOME之前卸载该服务。

  将ERLANG_HOME设置为实际放置Erlang的位置，

  > **例如 C：\ Program Files \ erl x.xx（完整路径）。**
  >
  > **RabbitMQ批处理文件期望执行 ％ERLANG_HOME％ \ bin \ erl.exe。**

  转到开始>设置>控制面板>系统>高级>环境变量。创建系统环境变量ERLANG_HOME 并将其设置为包含bin \ erl.exe的目录的完整路径 。

### 4.2 下载安装RabbitMQ服务器

- 下载 **rabbitmq-server-windows-3.7.13.zip**

  > 下载地址: https://github-production-release-asset-2e65be.s3.amazonaws.com/924551/e357e480-4

- **为RABBITMQ_SERVER设置环境环境变量**

  > **例如 D:\rabbitmq\rabbitmq-server-windows-3.7.13\rabbitmq_server-3.7.13**
  >
  > **RabbitMQ批处理文件期望执行 ％RABBITMQ_SERVER％ \ bin \ erl.exe。**

- **RabbitMQ 数据目录**

  默认情况下，RabbitMQ日志和节点的数据目录存储在当前用户的Application Data目录中，

  > **例如 C：\ Documents and Settings \ ％USERNAME％ \ Application Data或 C：\ Users \ ％USERNAME％ \ AppData \ Roaming。**

  在命令提示符处 执行echo％APPDATA％以查找此目录。或者，“开始”>“运行  ％APPDATA％” 将打开此文件夹。

  可以 使用以下环境变量之一将节点[配置](https://www.rabbitmq.com/configure.html)为使用[不同的数据目录](https://www.rabbitmq.com/relocate.html)：RABBITMQ_BASE，RABBITMQ_MNESIA_BASE或 RABBITMQ_MNESIA_DIR。请阅读[重定位指南](https://www.rabbitmq.com/relocate.html)，了解每个变量的工作原理。

- **RabbitMQ 安装和启动**

  ```bash
  rabbitmq-service.bat install
  ```
### 4.3 端口访问

防火墙和其他安全工具可能会阻止RabbitMQ绑定到端口。当发生这种情况时，RabbitMQ将无法启动。确保可以打开以下端口：

- 4369：[epmd](http://erlang.org/doc/man/epmd.html)，RabbitMQ节点和CLI工具使用的对等发现服务
- 5672,5671：AMQP 0-9-1和1.0客户端使用没有和使用TLS
- 25672：用于节点间和CLI工具通信（Erlang分发服务器端口），并从动态范围分配（默认情况下限于单个端口，计算为AMQP端口+ 20000）。除非确实需要这些端口上的外部连接（例如，群集使用[联合](https://www.rabbitmq.com/federation.html)或CLI工具在子网外的计算机上使用），否则不应公开这些端口。有关详情， 请参阅[网络指南](https://www.rabbitmq.com/networking.html)
- 35672-35682：由CLI工具（Erlang分发客户端端口）用于与节点通信，并从动态范围（计算为服务器分发端口+ 10000到服务器分发端口+ 10010）进行分配。有关详情， 请参阅[网络指南](https://www.rabbitmq.com/networking.html)
- 15672：[HTTP API](https://www.rabbitmq.com/management.html)客户端，[管理UI](https://www.rabbitmq.com/management.html)和[rabbitmqadmin](https://www.rabbitmq.com/management-cli.html)（仅当启用了[管理插件时](https://www.rabbitmq.com/management.html)）
- 61613,61614：没有和使用TLS的[STOMP客户端](https://stomp.github.io/stomp-specification-1.2.html)（仅当启用了[STOMP插件时](https://www.rabbitmq.com/stomp.html)）
- 1883,8883 :( 如果启用了[MQTT插件](https://www.rabbitmq.com/mqtt.html)，则没有和使用TLS的[MQTT客户端](http://mqtt.org/)
- 15674：STOMP-over-WebSockets客户端（仅当启用了[Web STOMP插件时](https://www.rabbitmq.com/web-stomp.html)）
- 15675：MQTT-over-WebSockets客户端（仅当启用了[Web MQTT插件时](https://www.rabbitmq.com/web-mqtt.html)）

可以将RabbitMQ配置为使用不同的端口。

- **默认用户访问权限**      

  > 用户名：guest
  >
  > 密码：guest

  ![1553621636952](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1553621636952.png)

![1553621656875](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1553621656875.png)

> *注意*：[详情配置请看官方文档](https://www.rabbitmq.com/install-windows-manual.html)

