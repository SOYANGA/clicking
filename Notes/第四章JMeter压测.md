# 第四章JMeter压测

## JMeter入门



### 官方网站：http://jmeter.apache.org/



### 下载地址：http://jmeter.apache.org/download_jmeter.cgi/



### 用户手册：http://jmeter.apache.org/usermanual/index.html



JMeter使用

1. 添加线程组
2. 添加监听器 ->聚合报告
3. 线程组右键-->添加Sampler（取样器）->Http请求

## 自定义变量模拟多用户

### 测试计划

1. 添加配置元件 CSV Data Set Config
2. 引用变量\${}

## JMeter命令行使用

1. 在windows上录好jmx
2. 命令行：sh jmeter.sh -n -t XXX.jmx -l result.jtl   linux上运行
3. 把result.jtl导入到jmeter



## Redis压测工具redis-benchmark

1. redis-benchmark -h 127.0.0.1 -p 6379 -c 100 -n 100000  100个并发连接，100000个请求

   ![1558235160994](C:\Users\32183\AppData\Roaming\Typora\typora-user-images\1558235160994.png)

2. redis-benchmark -h 127.0.0.1 -p 6379 -q -d 100 存取大小为100字节的数据包

   ![1558235265135](C:\Users\32183\AppData\Roaming\Typora\typora-user-images\1558235265135.png)

3. redis-benchmark -t set,lpush -n 100000 -q 只测试某些操作的性能

   ![1558235362081](C:\Users\32183\AppData\Roaming\Typora\typora-user-images\1558235362081.png)

4. redis-benchmark -n 100000 -q script load “redis.call(‘set’,‘foo’,‘bar’)”  只测试某些数值存取的性能

   ![1558235525487](C:\Users\32183\AppData\Roaming\Typora\typora-user-images\1558235525487.png)

## Spring Boot打war包

- 添加spring-boot-starter-tomcat 的provided依赖

```xml
        <!--添加Tomcat provide依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope>
        </dependency>
```

- 添加mavrn-war-plugin插件

```xml
  <!--SpringBoot的热加载  以及 构建可执行war-->
    <build>
        <finalName>${project.artifactId}</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <fork>true</fork>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>6</source>
                    <target>6</target>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>

        </plugins>
    </build>
```

- 修改主类

```
@SpringBootApplication
public class MainApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    //打成war包 Tomcat中会调用该方法启动
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainApplication.class);
    }
}
```



- 修改context-path路径

```properties
#修改端口号
server.port=8080
#下修改context-path路径
server.servlet.context-path=/
```

