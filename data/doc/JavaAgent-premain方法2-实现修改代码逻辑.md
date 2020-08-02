## JavaAgent-premain方法 实现修改代码逻辑

### 效果：

实现 修改 程序源代码 hello -> hello agented

#### Agent2.java

```java
public class Agent2 {
    /**
     * 可以运行在main方法启动前
     * @param agent             输入的参数
     * @param instrumentation             输入的参数
     */
    public static void premain(String agent, Instrumentation instrumentation){
        System.out.println("Agent2 premain 2param :" + agent);
        instrumentation.addTransformer(new ConsoleTransformer(),true);
    }

}
```

ConsoleTransformer.java
```java
public class ConsoleTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals("cn/bigfire/Console")){
            String root = StrUtil.subBefore(System.getProperty("user.dir"), "JavaAgentDemo", true);
            String classFile = root + "JavaAgentDemo/agent/src/main/resources/Console.class";
            return FileUtil.readBytes(classFile);
        }
        return classfileBuffer;
    }
}
```

#### Demo2.java

```java
public class Demo2 {

    /**
     * VM参数
     * -javaagent:D:\desktop\text\code\mycode\JavaAgentDemo\agent\target/agent.jar=input
     * */
    public static void main(String[] args) throws Exception {
        new Thread(()->{
            while (true){
                Console.hello();//    public static void hello(){System.out.println("hello"); }
                ThreadUtil.sleep(2000);
            }
        }).start();
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
Premain-Class: cn.bigfire.Agent2
Can-Retransform-Classes: true

```

#### 运行效果
```shell script
Agent2 premain 2param :input
满足条件
hello  agented
hello  agented
hello  agented
hello  agented
```



#### 测试Agent2
复制`MANIFEST.MF_Agent2`到`META-INF`目录下。去掉后缀`_Agent2`覆盖MANIFEST.MF文件

```shell script
cd 【项目目录】JavaAgentDemo\agent 
mvn clean package
```

![overwrite](https://gitee.com/dahuoyzs/res/raw/master/img/overwrite.png)

![mavenpackage](https://gitee.com/dahuoyzs/res/raw/master/img/mavenpackage.jpg)


启动demo2 设置JVM参数

`-javaagent:【项目目录】JavaAgentDemo\agent\target/agent.jar=input`

![image-20200731201651853](https://gitee.com/dahuoyzs/res/raw/master/img/image-20200731201651853.png)



