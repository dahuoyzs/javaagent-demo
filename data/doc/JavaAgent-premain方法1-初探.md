## JavaAgent-premain方法 初探

### 效果：

实现main方法前执行业务逻辑

#### Agent1.java

```java
public class Agent1 {
    public static void premain(String agent){
        System.out.println("Agent1 premain :" + agent);
    }
}
```

#### Demo1.java

```java
public class Demo1 {

    /**
     * VM参数
     * -javaagent:D:\desktop\text\code\mycode\JavaAgentDemo\agent\target/agent.jar=input
     * */
    public static void main(String[] args) throws Exception {
        System.out.println("demo1");
    }
}
```

#### resources/META-INF/MANIFEST.MF

```META-INF
Manifest-Version: 1.0
Archiver-Version: Plexus Archiver
Built-By: dahuoyzs
Created-By: Apache Maven 3.6.0
Build-Jdk: 1.8.0_171
Premain-Class: cn.bigfire.Agent1
Can-Retransform-Classes: true

```

#### 运行效果

```shell script
Agent1 premain :input
demo1
```





### 源码测试Agent1

#### 复制

`MANIFEST.MF_Agent1`到`META-INF`目录下。去掉后缀`_Agent1`覆盖MANIFEST.MF文件

```shell script
cd 【项目目录】JavaAgentDemo\agent 
mvn clean package
```
![overwrite](https://gitee.com/dahuoyzs/res/raw/master/img/overwrite.png)

![mavenpackage](https://gitee.com/dahuoyzs/res/raw/master/img/mavenpackage.jpg)



#### 启动demo1 设置JVM参数

`-javaagent:【项目目录】JavaAgentDemo\agent\target/agent.jar=input`

![image-20200731201651853](https://gitee.com/dahuoyzs/res/raw/master/img/image-20200731201651853.png)








