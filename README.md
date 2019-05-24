# :crossed_fingers:clicking

<center><b>When you're ready to buy a seckill item, you click the seckill button repeatedly</b></center> 

[![项目作者](https://img.shields.io/badge/author-soyanga-orange.svg)](https://github.com/SOYANGA) ![项目使用语言](https://img.shields.io/badge/language-java-yellow.svg) ![JDK版本](https://img.shields.io/badge/JDK-1.8-brightgreen.svg) ![项目状态](https://img.shields.io/badge/clicking-doing-green.svg) ![项目进度](http://progressed.io/bar/80?title=progress)

## 项目介绍：

Clicking是一个基于SpringBoot实现的秒杀系统，通过一些优化，提升其应对高并发的处理能力。

## 写在最前面

该项目主要能体现了应对高并发，大流量场景的秒杀系统的解决方案。（初学者入门）

## 项目效果展示

![ProjectPresentations3](https://github.com/SOYANGA/clicking/blob/master/img/ProjectPresentations.gif)

## 项目的基本结构

<details>
<summary>展开查看</summary>
<pre><code>
.
├─main
│  ├─java
│  │  └─com
│  │      └─github
│  │          └─soyanga
│  │              └─secondskillsystem
│  │                  ├─access  	-- 防刷限流组件
│  │                  ├─config		-- 方法参数解析组件
│  │                  ├─controller	-- MVC的web层
│  │                  ├─dao			-- 数据库操作
│  │                  ├─domain		-- 实体类
│  │                  ├─exception	-- 全局异常处理
│  │                  ├─rabbitmq	-- RabbitMq组件
│  │                  ├─redis		-- Redis,Jedis 相关缓存配置
│  │                  ├─result		-- 请求结果封装
│  │                  ├─service		-- 服务层实现
│  │                  ├─util		-- 工具组件
│  │                  ├─Validator	-- 登陆号码校验组件
│  │                  └─vo			-- 返回视图整合处理
│  └─resources
│      ├─static
│      │  ├─bootstrap
│      │  │  ├─css
│      │  │  ├─fonts
│      │  │  └─js
│      │  ├─img		--图片资源
│      │  ├─jquery-validation
│      │  │  └─localization
│      │  ├─js
│      │  └─layer
│      │      ├─mobile
│      │      │  └─need
│      │      └─skin
│      │          └─default
│      └─templates	-- 存放Thymeleaf模板引擎所需的HTML
└─test
    └─java	-- 测试文件
</code></pre>
</details>


## 具体技术栈

- 前端:JQuery+Bootstrap+Thymeleaf

- 后端:SpringBoot-2.1.4+JSR303服务端验证框架+MyBatis

- 中间件:RabbitMQ+Redis+Druid

## 项目搭建环境

- IDEA+Maven+Tomcat+JDK8+Windows10

## 如何启动

1. 直接将[last version](https://github.com/SOYANGA/clicking/tree/master/latest%20version/seconds_kill_system%20_8)文件导入IDEA即可，Maven提示选择自动导入依赖。
2. 启动前请根据本地环境配置好[application.properties](https://github.com/SOYANGA/clicking/blob/master/latest%20version/seconds_kill_system%20_8/src/main/resources/application.properties)中的**数据库，Redis，Rabbit**相关配置 (eg:地址，端口号)。
3. 启动前请先创建seckill数据库,数据库建表语句放在：[/mysql/seckill_sql.sql](https://github.com/SOYANGA/clicking/blob/master/mysql/seckill_sql.sql)文件内。运行前务必先在插入**秒杀用户信息**、**商品信息**、**秒杀商品信息**、**秒杀开始结束时间**（…因为目前还没有做注册功能）。:busts_in_silhouette:
4. 以上3步做好后，运行`src/main/java/com/github/soyanga/secondskillsystem`下的`/MainApplication.java`中的main方法访问[http://localhost:8080/login/to_login](http://localhost:8080/login/to_login)即可进入登陆页面。
5. 本项目运行环境默认是在windows下的，如需部署到服务器上则根据实际情况自行配置相关参数。:yum:

## 秒杀系统优化

秒杀功能基本实现后使用JMeter对项目进行了压测，发现qps并不是很理想，且出现了超买。

根据项目分析发现性能瓶颈主要是在数据库上，所有秒杀请都会访问数据库，数据库并不能承受这么高的并发量，我适当调节了数据库连接池Druid的参数，并没有对性能上有太多的提升。所以如下分别从页面，秒杀接口上入手，减少对数据库的访问。并且对系统安全上进行了优化

### 页面优化：

1. 页面缓存+URL缓存+对象缓存
2. 页面静态化，前后端分离
3. 静态资源的优化(图片 CSS,JS)
   - JS/CSS压缩，减少流量(项目中并未具体实现，只是利用了SpringBoot.resources)
   - 多个JS/CSS组合一个请求，减少连接数(并未具体实现)
- CDN优化
   - CDN服务器就近访问,服务器轮询（可以进一步优化的点）

### 秒杀接口优化：

1. Redis预减库存减少数据库访问
2. 内存标记减少Redis访问
3. RabbitMQ队列缓冲，异步下单，增强用户体验
- Nginx水平扩展（没有实施环境，只是概念层假设）
- LVS

### 安全优化：

1. 秒杀接口地址隐藏
2. 数学公式的验证码 （削峰）
3. 接口防刷  类似限流

### 解决超买

1. 数据库加唯一索引：防止用户重复购买（添加事务，数据库添加失败就回滚并不会创建订单）
2. SQL加库存数量判断：防止库存变成负数

## 项目执行流程

![ProjectPresentations3](https://github.com/SOYANGA/clicking/blob/master/img/clicking项目流程图.png)

## 项目详细搭建过程，具体技术实现（…正在整理完善 :happy:)

[![我的博客](https://img.shields.io/badge/Blog-@SOYANGA-red.svg)](https://soyanga.github.io/)

