---
typora-root-url: document
---

# Apache-Dubbo

# 一、基础知识

## 1、分布式基础理论

### 1.1）、什么是分布式系统？

cap+base理论

《分布式系统原理与范型》定义：

“分布式系统是若干独立计算机的集合，这些计算机对于用户来说就像单个相关系统”

分布式系统（distributed system）是建立在网络之上的软件系统。

随着互联网的发展，网站应用的规模不断扩大，常规的垂直应用架构已无法应对，分布式服务架构以及流动计算架构势在必行，亟需一个治理系统确保架构有条不紊的演进。

高并发:同一时间点请求流量洪峰

高可用:确保服务可用

高扩展:机器节点添加时耦合度降低

### 1.2）、发展演变

![img](Apache-Dubbo.assets/wps6-1600944967012.png) 

#### 单一应用架构

当网站流量很小时，只需一个应用，将所有功能都部署在一起，以减少部署节点和成本。此时，用于简化增删改查工作量的数据访问框架(ORM)是关键。 

![image-20201011074440286](Apache-Dubbo.assets/image-20201011074440286.png)

适用于小型网站，小型管理系统，将所有功能都部署到一个功能里，简单易用。

缺点： 

​	1、性能扩展比较难 

​    2、协同开发问题

​    3、不利于升级维护

#### 垂直应用架构

当访问量逐渐增大，单一应用增加机器带来的加速度越来越小，将应用拆成互不相干的几个应用，以提升效率。此时，用于加速前端页面开发的Web框架(MVC)是关键。

![image-20201011074504205](Apache-Dubbo.assets/image-20201011074504205.png)

通过切分业务来实现各个模块独立部署，降低了维护和部署的难度，团队各司其职更易管理，性能扩展也更方便，更有针对性。

缺点： 公用模块无法重复利用，开发性的浪费

#### 分布式服务架构

当垂直应用越来越多，应用之间交互不可避免，将核心业务抽取出来，作为独立的服务，逐渐形成稳定的服务中心，使前端应用能更快速的响应多变的市场需求。此时，用于提高业务复用及整合的分布式服务框架(RPC)是关键。

![image-20201011074519467](Apache-Dubbo.assets/image-20201011074519467.png)

![image-20201011080149388](Apache-Dubbo.assets/image-20201011080149388.png)

#### 流动计算架构

当服务越来越多，容量的评估，小服务资源的浪费等问题逐渐显现，此时需增加一个调度中心基于访问压力实时管理集群容量，提高集群利用率。此时，用于提高机器利用率的资源调度和治理中心(SOA)是关键。

![image-20201011074812349](Apache-Dubbo.assets/image-20201011074812349.png)



### 1.3）RPC

#### 什么叫RPC

​	RPC【Remote Procedure Call】是指远程过程调用，是一种进程间通信方式，他是一种技术的思想，而不是规范。它允许程序调用另一个地址空间（通常是共享网络的另一台机器上）的过程或函数，而不用程序员显式编码这个远程调用的细节。即程序员无论是调用本地的还是远程的函数，本质上编写的调用代码基本相同。

#### RPC基本原理

![img](Apache-Dubbo.assets/wps11.jpg)  

RPC两个核心模块：通讯，序列化。

![image-20201011075046070](Apache-Dubbo.assets/image-20201011075046070.png)

## 2、dubbo核心概念

### 2.1）、简介

Apache Dubbo (incubating) |ˈdʌbəʊ| 是一款高性能、轻量级的开源Java RPC框架，它提供了三大核心能力：面向接口的远程方法调用，智能容错和负载均衡，以及服务自动注册和发现。

springMVC handler

浏览器:http://xxx:xx/order/xxxx

service:普通方法

官网：

http://dubbo.apache.org/

![image-20201011080507012](Apache-Dubbo.assets/image-20201011080507012.png)

### 2.2）、基本概念

![img](Apache-Dubbo.assets/wps13.jpg) 

服务提供者（Provider）：暴露服务的服务提供方，服务提供者在启动时，向注册中心注册自己提供的服务。

服务消费者（Consumer）: 调用远程服务的服务消费方，服务消费者在启动时，向注册中心订阅自己所需的服务，服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。

​	注册中心（Registry）：注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者

​	监控中心（Monitor）：服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心

 调用关系说明

l 服务容器负责启动，加载，运行服务提供者。

l 服务提供者在启动时，向注册中心注册自己提供的服务。

l 服务消费者在启动时，向注册中心订阅自己所需的服务。

l 注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者。

l 服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。

l 服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。

## 3、dubbo环境

## 搭建

### 3.1）Linux -安装zookeeper 

#### 1、安装zookeeper

1、下载zookeeper网址 https://archive.apache.org/dist/zookeeper/zookeeper-3.4.11/ 

​	wget https://archive.apache.org/dist/zookeeper/zookeeper-3.4.11/zookeeper-3.4.11.tar.gz

2、解压![img](Apache-Dubbo.assets/wps22.jpg)

3、移动到指定位置并改名为zookeeper![img](Apache-Dubbo.assets/wps23.jpg) 

4、初始化zookeeper配置文件拷贝/usr/local/zookeeper/conf/zoo_sample.cfg  到同一个目录下改个名字叫zoo.cfg![img](Apache-Dubbo.assets/wps28.jpg)

5、配置

![image-20200925085853581](Apache-Dubbo.assets/image-20200925085853581.png)

6、启动zookeeper

![image-20200925090228382](Apache-Dubbo.assets/image-20200925090228382.png)

7、停止zookeeper

​		![image-20200925090250577](Apache-Dubbo.assets/image-20200925090250577.png)

8、查看zookeeper状态

​	![image-20200925090320667](Apache-Dubbo.assets/image-20200925090320667.png)

### 3.2）、Linux -安装dubbo-admin管理控制台

#### 1、安装Tomcat8（旧版dubbo-admin是war，新版是jar不需要安装Tomcat）

略......

#### 2、安装dubbo-admin

dubbo本身并不是一个服务软件。它其实就是一个jar包能够帮你的java程序连接到zookeeper，并利用zookeeper消费、提供服务。所以你不用在Linux上启动什么dubbo服务。

但是为了让用户更好的管理监控众多的dubbo服务，官方提供了一个可视化的监控程序，不过这个监控即使不装也不影响使用。

1、下载dubbo-adminhttps://github.com/apache/incubator-dubbo-ops ![img](Apache-Dubbo.assets/wps36.jpg)

2、将dubbo-admin.war上传至 /tomcat/webapps/ 目录下

3、进入目录，修改zk注册中心地址

![image-20200925091142361](Apache-Dubbo.assets/image-20200925091142361.png)

4、查看地址

![image-20200925091217723](Apache-Dubbo.assets/image-20200925091217723.png)

5、启动tomcat访问 http://192.168.153.131:8080/dubbo-admin/

运行dubbo-adminjava -jar dubbo-admin-0.0.1-SNAPSHOT.jar默认使用root/root 登陆

![img](Apache-Dubbo.assets/wps38.jpg)



## 4、dubbo初体验

### 4.1）、提出需求

某个电商系统，订单服务需要调用用户服务获取某个用户的所有地址；

我们现在 需要创建两个服务模块进行测试 

| 模块            | 功能      |
| ------------- | ------- |
| 订单服务web模块     | 创建订单等   |
| 用户服务service模块 | 查询用户地址等 |

测试预期结果：

​	订单服务web模块在A服务器，用户服务模块在B服务器，A可以远程调用B的功能。

### 4.2）、工程架构

根据 dubbo《服务化最佳实践》 

#### 1、分包

建议将服务接口，服务模型，服务异常等均放在 API 包中，因为服务模型及异常也是 API 的一部分，同时，这样做也符合分包原则：重用发布等价原则(REP)，共同重用原则(CRP)。

如果需要，也可以考虑在 API 包中放置一份 spring 的引用配置，这样使用方，只需在 spring 加载过程中引用此配置即可，配置建议放在模块的包目录下，以免冲突，如：com/alibaba/china/xxx/dubbo-reference.xml。

#### 2、粒度

服务接口尽可能大粒度，每个服务方法应代表一个功能，而不是某功能的一个步骤，否则将面临分布式事务问题，Dubbo 暂未提供分布式事务支持。

服务接口建议以业务场景为单位划分，并对相近业务做抽象，防止接口数量爆炸。

不建议使用过于抽象的通用接口，如：Map query(Map)，这样的接口没有明确语义，会给后期维护带来不便。

![img](Apache-Dubbo.assets/wps39.jpg) 

![image-20201011082805324](Apache-Dubbo.assets/image-20201011082805324.png)

### 4.3）、创建模块

#### 1、gmall-interface：公共接口层（model，service，exception…）

作用：定义公共接口，也可以导入公共依赖

1、Bean模型

```java
@Data
@AllArgsConstructor
public class UserAddress implements Serializable{   
    private Integer id;   
    private String userAddress;   
    private String userId;   
    private String consignee;   
    private String phoneNum;   
    private String isDefault;
}
```

2、Service接口

```java
public interface UserService {
    List<UserAddress> getUserAddressList(String userId);
}
```

![image-20200925092600773](Apache-Dubbo.assets/image-20200925092600773.png)

#### 2、gmall-user：用户模块（对用户接口的实现）

1、pom.xml 

```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>gmail_interface</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

2、Service

```java
public class UserServiceImpl implements UserService {

    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        //Integer id, String userAddress, String userId, String consignee, String phoneNum, String isDefault
        UserAddress userAddress = new UserAddress(1,"上海松江","1","mm","15600000000","1");
        UserAddress userAddress1 = new UserAddress(2,"上海松江","2","hh","15600000001","1");
        return Arrays.asList(userAddress,userAddress1);
    }
}
```

![image-20200925093134074](Apache-Dubbo.assets/image-20200925093134074.png)

#### 4、gmall-order-web：订单模块（调用用户模块）

1、pom.xml

```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>gmail_interface</artifactId>
    <version>1.0-SNAPSHOT</version>
    </dependency>
```

2、调用用户

```java
public interface OrderServcie {

    void initOrder(String userId);
}

public class OrderServiceImpl implements OrderServcie {

    @Autowired
    UserService userService;

    @Override
    public void initOrder(String userId) {
        List<UserAddress> userAddresses = userService.getUserAddressList("1");
        System.err.println(userAddresses);
    }
}

```

![image-20200925093611885](Apache-Dubbo.assets/image-20200925093611885.png)

现在这样是无法进行调用的。我们gmall-order-web引入了gmall-interface，但是interface的实现是gmall-user，我们并没有引入，而且实际他可能还在别的服务器中。

### 4.4）、使用dubbo改造

#### 1、改造gmall-user作为服务提供者

1、引入dubbo

```xml
<!-- 引入dubbo -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>dubbo</artifactId>
    <version>2.6.2</version>
</dependency>

<!-- 由于我们使用zookeeper作为注册中心，
        所以需要操作zookeeperdubbo 2.6以前的版本引入zkclient操作zookeeper
        dubbo 2.6及以后的版本引入curator操作zookeeper下面两个zk客户端根据dubbo版本2选1即可-->
<dependency>
    <groupId>com.101tec</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.10</version>
</dependency>
<!-- curator-framework -->
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>2.12.0</version>
</dependency>
```

2、配置提供者

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 提供方应用信息，用于计算依赖关系 -->
    <dubbo:application name="gmall-user"  />

    <!-- 使用zk广播注册中心暴露服务地址 -->
    <dubbo:registry address="zookeeper://192.168.153.131:2181" />

    <!-- 用dubbo协议在20880端口暴露服务 -->
    <dubbo:protocol name="dubbo" port="20880" />

    <!-- 声明需要暴露的服务接口 -->
    <dubbo:service interface="com.atguigu.service.UserService" ref="userService" />

    <!-- 和本地bean一样实现服务 -->
    <bean id="userService" class="com.atguigu.service.impl.UserServiceImpl" />

</beans>
```

3、启动服务

```java
public class UserMainApplication {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:provider.xml");
        System.in.read();
    }

}

```

#### 2、改造gmall-order-web作为服务消费者

1.pom.xml

```xml
 <!-- 引入dubbo -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>2.6.2</version>
        </dependency>
        <!-- 由于我们使用zookeeper作为注册中心，所以需要引入zkclient和curator操作zookeeper -->
        <dependency>
            <groupId>com.101tec</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.10</version>
        </dependency>
             <!-- curator-framework -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>2.12.0</version>
        </dependency>
```

2、配置消费者信息

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样 -->
    <dubbo:application name="gmall-order-web"  />

    <!-- 使用multicast广播注册中心暴露发现服务地址 -->
    <dubbo:registry address="zookeeper://192.168.153.131:2181" />

    <!-- 生成远程服务代理，可以和本地bean一样使用demoService -->
    <dubbo:reference id="userService" interface="com.atguigu.service.UserService" />

    <!--扫描包-->
    <context:component-scan base-package="com.atguigu"/>
</beans>
```

#### 3、测试调用

访问gmall-order-web的initOrder请求，会调用UserService获取用户地址；

```java
public class OrderMainApplication {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:consumer.xml");
        OrderServcie orderServcie = context.getBean(OrderServcie.class);
        orderServcie.initOrder("1");
        System.in.read();
    }
}
```

调用成功。说明我们order已经可以调用远程的UserService了；

#### 4、注解版

1.生产者

```xml
<dubbo:application name="gmall-user"></dubbo:application>  
<dubbo:registry address="zookeeper://192.168.153.131:2181" />
<dubbo:protocol name="dubbo" port="20880" />
<dubbo:annotation package="com.atguigu.service.impl"></dubbo:annotation>
```

```java
@Service  //使用Service注解
public class UserServiceImpl implements UserService {

    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        //Integer id, String userAddress, String userId, String consignee, String phoneNum, String isDefault
        UserAddress userAddress = new UserAddress(1,"上海松江","1","mm","15600000000","1");
        UserAddress userAddress1 = new UserAddress(2,"上海松江","2","hh","15600000001","1");
        return Arrays.asList(userAddress,userAddress1);
    }
}
```

2.服务消费方

```xml
<dubbo:application name="gmall-order-web"></dubbo:application>
<dubbo:registry address="zookeeper://192.168.153.131:2181" />
<dubbo:annotation package="com.atguigu.service.impl"/>
<context:component-scan base-package="com.atguigu"/>
```

```java
@Service
public class OrderServiceImpl implements OrderServcie {

    @Reference  //使用此注解引用dubbo服务
    UserService userService;

    @Override
    public void initOrder(String userId) {
        List<UserAddress> userAddresses = userService.getUserAddressList("1");
        System.err.println(userAddresses);
    }
}
```

## 5、监控中心

### 5.1、dubbo-admin

图形化的服务管理页面；安装时需要指定注册中心地址，即可从注册中心中获取到所有的提供者/消费者进行配置管理

### 5.2、dubbo-monitor-simple

简单的监控中心；

#### 1、安装

1、下载 dubbo-opshttps://github.com/apache/incubator-dubbo-ops 

2、修改配置指定注册中心地址进入 dubbo-monitor-simple\src\main\resources\conf修改 dubbo.properties文件

![image-20201011085652211](Apache-Dubbo.assets/image-20201011085652211.png)

3、上传至Linux   rz 

4、解压

unzip -d /usr/local/ dubbo-monitor-simple-2.0.0.zip

5、启动

[root@0713 assembly.bin]# ./start.sh

6、访问

![image-20201011090126656](Apache-Dubbo.assets/image-20201011090126656.png)

#### 2、配置监控中心

生产者与消费者均配置

```xml
<!--监控中心-->
<dubbo:monitor address="192.168.153.131:8181"></dubbo:monitor>
<!--自动发现-->
<dubbo:monitor protocol="registry"></dubbo:monitor>
```

# 二、dubbo配置

## 1、配置原则

![image-20201011091253888](Apache-Dubbo.assets/image-20201011091253888.png)

![img](Apache-Dubbo.assets/wps45-1600944967016.png) 

- JVM 启动 -D 参数优先，这样可以使用户在部署和启动时进行参数重写，比如在启动时需改变协议的端口。

  ![image-20200925111455185](Apache-Dubbo.assets/image-20200925111455185.png)

- XML 次之，如果在 XML 中有配置，则 dubbo.properties 中的相应配置项无效。

![image-20200925111516532](Apache-Dubbo.assets/image-20200925111516532.png)

- Properties 最后，相当于缺省值，只有 XML 没有配置时，dubbo.properties 的相应配置项才会生效，通常用于共享公共配置，比如应用名。

![image-20200925111537817](Apache-Dubbo.assets/image-20200925111537817.png)

![image-20200925111543724](Apache-Dubbo.assets/image-20200925111543724.png)

## 2、启动时检查

Dubbo 缺省会在启动时检查依赖的服务是否可用，不可用时会抛出异常，阻止 Spring 初始化完成，以便上线时，能及早发现问题，默认 `check="true"`。

可以通过 `check="false"` 关闭检查，比如，测试时，有些服务不关心，或者出现了循环依赖，必须有一方先启动。

另外，如果你的 Spring 容器是懒加载的，或者通过 API 编程延迟引用服务，请关闭 check，否则服务临时不可用时，会抛出异常，拿到 null 引用，如果 `check="false"`，总是会返回引用，当服务恢复时，能自动连上。

注意加入log4j日志

1:在注解上

![image-20200925122356528](Apache-Dubbo.assets/image-20200925122356528.png)

2:在xml

![image-20200925122428823](Apache-Dubbo.assets/image-20200925122428823.png)

## 3、超时时间

由于网络或服务端不可靠，会导致调用出现一种不确定的中间状态（超时）。为了避免超时导致客户端资源（线程）挂起耗尽，必须设置超时时间。

1、服务提供者设置超时时间

![image-20200925162855465](Apache-Dubbo.assets/image-20200925162855465.png)

2、消费者远程调用报错

![image-20200925162958219](Apache-Dubbo.assets/image-20200925162958219.png)

3、生产者默认超时时间是1000

![image-20200925163040342](Apache-Dubbo.assets/image-20200925163040342.png)

### 3.1、Dubbo消费端 

```xml
全局超时配置:
<dubbo:consumer timeout="5000" /> 
指定接口以及特定方法超时配置,注意将服务自动发现关闭
<dubbo:reference interface="com.foo.BarService" timeout="2000"> 
    <dubbo:method name="sayHello" timeout="3000" />
</dubbo:reference>
```

### 3.2、Dubbo服务端 

```xml
全局超时配置:
<dubbo:provider timeout="5000" /> 
指定接口以及特定方法超时配置:
<dubbo:provider interface="com.foo.BarService" timeout="2000">  
    <dubbo:method name="sayHello" timeout="3000" />
</dubbo:provider>
```

### 3、配置原则

dubbo推荐在Provider上尽量多配置Consumer端属性：

1、作服务的提供者，比服务使用方更清楚服务性能参数，如调用的超时时间，合理的重试次数，等等2、在Provider配置后，Consumer不配置则会使用Provider的配置值，即Provider配置可以作为Consumer的缺省值。否则，Consumer会使用Consumer端的全局设置，这对于Provider不可控的，并且往往是不合理的

配置的覆盖规则：

1. 方法级配置别优于接口级别，即小Scope优先 
2. Consumer端配置 优于 Provider配置 优于 全局配置
3. 最后是Dubbo Hard Code的配置值（见配置文档）

![img](Apache-Dubbo.assets/wps46-1600944967016.png)

```
1:方法级优先，接口级次之，全局配置再次之。
2:如果级别一样，则消费方优先，提供方次之
3:如果级别不一样,则精确的一方优先
```

## 4、重试次数

失败自动切换，当出现失败，重试其它服务器，但重试会带来更长延迟。可通过 retries="2" 来设置重试次数(不含第一次)。

1、消费者重试次数配置如下：

```xml
<dubbo:reference id="userService" interface="com.atguigu.service.UserService"
                 check="false" retries="2">
    <dubbo:method name="getUserAddressList" timeout="1000"/>
</dubbo:reference>
```

 2、生产者控制台打印查看输出

```java
@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        System.err.println("provider getUserAddressList");
        try {
            //设置休眠时间
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Integer id, String userAddress, String userId, String consignee, String phoneNum, String isDefault
        UserAddress userAddress = new UserAddress(1,"上海松江","1","mm","15600000000","1");
        UserAddress userAddress1 = new UserAddress(2,"上海松江","2","hh","15600000001","1");
        return Arrays.asList(userAddress,userAddress1);
    }
}
```

3:发现

![image-20200925170356911](Apache-Dubbo.assets/image-20200925170356911.png)

#### 客户端集群操作

provider加入3份,查看控制台输出

实际应用时需要注意幂等

幂等[add]

非幂等[select,update,delete]

## 4、版本号

当一个接口实现，出现不兼容升级时，可以用版本号过渡，版本号不同的服务相互间不引用。

可以按照以下的步骤进行版本迁移：

在低压力时间段，先升级一半提供者为新版本

再将所有消费者升级为新版本

然后将剩下的一半提供者升级为新版本

1:生产者设置

![image-20200925173438664](Apache-Dubbo.assets/image-20200925173438664.png)

2:消费者设置

1.0.0   2.0.0  *(随机)

![image-20200925173522114](Apache-Dubbo.assets/image-20200925173522114.png)

## 5、本地存根

远程服务后，客户端通常只剩下接口，而实现全在服务器端，但提供方有些时候想在客户端也执行部分逻辑，比如：做 ThreadLocal 缓存，提前验证参数，调用失败后伪造容错数据等等，此时就需要在 API 中带上 Stub，客户端生成 Proxy 实例，会把 Proxy 通过构造函数传给 Stub [[1\]](http://dubbo.apache.org/zh-cn/docs/user/demos/local-stub.html#fn1)，然后把 Stub 暴露给用户，Stub 可以决定要不要去调 Proxy。

![image-20200925180608671](Apache-Dubbo.assets/image-20200925180608671.png)

1:编写代码

![image-20200926111911938](Apache-Dubbo.assets/image-20200926111911938.png)

2:配置

```xml
<!-- stub:存根 -->
<dubbo:reference id="userService" interface="com.atguigu.service.UserService" stub="com.atguigu.service.UserServiceStub">
    <dubbo:method name="getUserAddressList" timeout="1000"/>
</dubbo:reference>
```

# 三、高可用

## 1、zookeeper宕机与dubbo直连

现象：zookeeper注册中心宕机，还可以消费dubbo暴露的服务。

原因：

健壮性

l 监控中心宕掉不影响使用，只是丢失部分采样数据

l 数据库宕掉后，注册中心仍能通过缓存提供服务列表查询，但不能注册新服务SpringCloud nacos

l 注册中心对等集群，任意一台宕掉后，将自动切换到另一台

l <font style="color:red">注册中心全部宕掉后，服务提供者和服务消费者仍能通过本地缓存通讯</font>

l 服务提供者无状态，任意一台宕掉后，不影响使用

l 服务提供者全部宕掉后，服务消费者应用将无法使用，并无限次重连等待服务提供者恢复 

高可用：通过设计，减少系统不能提供服务的时间；

注意:dubbo直连.绕过zk(只能调用1.0.0版本),由于没有zk调度中心,所以默认连接第一个版本

![image-20200925174513966](Apache-Dubbo.assets/image-20200925174513966.png)

## 2、集群下dubbo负载均衡配置

在集群负载均衡时，Dubbo 提供了多种均衡策略，缺省为 random 随机调用。

负载均衡策略

Random LoadBalance随机，按权重设置随机概率。在一个截面上碰撞的概率高，但调用量越大分布越均匀，而且按概率使用权重后也比较均匀，有利于动态调整提供者权重。

![image-20200926112837668](Apache-Dubbo.assets/image-20200926112837668.png)

RoundRobin LoadBalance轮循，按公约后的权重设置轮循比率。存在慢的提供者累积请求的问题，比如：第二台机器很慢，但没挂，当请求调到第二台时就卡在那，久而久之，所有请求都卡在调到第二台上。

![image-20200926112849774](Apache-Dubbo.assets/image-20200926112849774.png)

LeastActive LoadBalance最少活跃调用数，相同活跃数的随机，活跃数指调用前后计数差。使慢的提供者收到更少请求，因为越慢的提供者的调用前后计数差会越大。

![image-20200926112858064](Apache-Dubbo.assets/image-20200926112858064.png)

ConsistentHash LoadBalance一致性 Hash，相同参数的请求总是发到同一提供者。当某一台提供者挂时，原本发往该提供者的请求，基于虚拟节点，平摊到其它提供者，不会引起剧烈变动。

![image-20200926112911906](Apache-Dubbo.assets/image-20200926112911906.png)

1:生产者集群三份

20880 	20881 	20882

2:消费者

```xml
<!--
        loadbalance:负载均衡
            random: 权重随机
            leastactive:响应时间
            roundrobin:轮训
    -->
<dubbo:reference id="userService" interface="com.atguigu.service.UserService" loadbalance="random">
    <dubbo:method name="getUserAddressList" timeout="1000"/>
</dubbo:reference>
```

在控制台配置权重

![image-20200926115403511](Apache-Dubbo.assets/image-20200926115403511.png)

## 3、整合hystrix，服务熔断与降级处理

### 1、服务降级

什么是服务降级？

当服务器压力剧增的情况下，根据实际业务情况及流量，对一些服务和页面有策略的不处理或换种简单的方式处理，从而释放服务器资源以保证核心交易正常运作或高效运作。

可以通过服务降级功能临时屏蔽某个出错的非关键服务，并定义降级后的返回策略。

向注册中心写入动态配置覆盖规则：

```java
RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
Registry registry = 
  registryFactory.getRegistry(URL.valueOf("zookeeper://192.168.153.131:2181"));
registry.register(URL.valueOf("override://0.0.0.0/com.foo.BarService?category=configurators&dynamic=false&application=foo&mock=force:return+null"));
```

其中：

mock=force:return+null 表示消费方对该服务的方法调用都直接返回 null 值，不发起远程调用。用来屏蔽不重要服务不可用时对调用方的影响。

![image-20200926120823587](Apache-Dubbo.assets/image-20200926120823587.png)

还可以改为 mock=fail:return+null 表示消费方对该服务的方法调用在失败后，再返回 null 值，不抛异常。用来容忍不重要服务不稳定时对调用方的影响。

![image-20200926120837380](Apache-Dubbo.assets/image-20200926120837380.png)

### 2、集群容错

在集群调用失败时，Dubbo 提供了多种容错方案，缺省为 failover 重试。

集群容错模式

```xml
Failover Cluster失败自动切换，当出现失败，重试其它服务器。通常用于读操作，但重试会带来更长延迟。可通过 retries="2" 来设置重试次数(不含第一次)。
重试次数配置如下：
<dubbo:service retries="2" />
或
<dubbo:reference retries="2" />
或
<dubbo:reference>  
    <dubbo:method name="findFoo" retries="2" />
</dubbo:reference> 

Failfast Cluster快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录。 

Failsafe Cluster失败安全，出现异常时，直接忽略。通常用于写入审计日志等操作。 

Failback Cluster失败自动恢复，后台记录失败请求，定时重发。通常用于消息通知操作。 

Forking Cluster并行调用多个服务器，只要一个成功即返回。通常用于实时性要求较高的读操作，但需要浪费更多服务资源。可通过 forks="2" 来设置最大并行数。 

Broadcast Cluster广播调用所有提供者，逐个调用，任意一台报错则报错 [2]。通常用于通知所有提供者更新缓存或日志等本地资源信息。 

集群模式配置按照以下示例在服务提供方和消费方配置集群模式
<dubbo:service cluster="failsafe" />
或
<dubbo:reference cluster="failsafe" />
```