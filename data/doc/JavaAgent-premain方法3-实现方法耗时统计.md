## JavaAgent-premain方法 无侵入动态修改程序源代码实现方法耗时统计

### 效果：

实现main方法外的所有方法统计时间



#### Agent3.java

```java
public class Agent3 {
    /**
     * 可以运行在main方法启动前
     * @param agent                         输入的参数
     * @param instrumentation               instrumentation对象由JVM提供并传入
     */
    public static void premain(String agent, Instrumentation instrumentation) {
        System.out.println("Agent3 premain :" + agent);
        instrumentation.addTransformer(new TimeCountTransformer());
    }

    /**
     * 时间统计Transformer  给要代理的方法添加时间统计
     */
    private static class TimeCountTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            try {
                className = className.replace("/", ".");
                if (className.equals("cn.bigfire.Demo3")) {
                    //使用全称,用于取得字节码类<使用javassist>
                    CtClass ctclass = ClassPool.getDefault().get(className);
                    //获得方法列表
                    CtMethod[] methods = ctclass.getDeclaredMethods();
                    //给方法设置代理
                    Stream.of(methods).forEach(method-> agentMethod(ctclass,method));
                    //CtClass转byte[]数组
                    return ctclass.toBytecode();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 代理方法，把传入的方法经写代理，并生成带时间统计的方法，
     * @param ctClass                         javassist的Class类
     * @param ctMethod                        javassist的ctMethod方法
     * */
    public static void agentMethod(CtClass ctClass,CtMethod ctMethod){
        try {
            String mName = ctMethod.getName();
            if (!mName.equals("main")){//代理除了main方法以外的所有方法
                String newName = mName + "$Agent";
                ctMethod.setName(newName);
                CtMethod newMethod = CtNewMethod.copy(ctMethod, mName, ctClass, null);
                // 构建新的方法体
                String bodyStr = "{\n" +
                        "long startTime = System.currentTimeMillis();\n" +
                        newName + "();\n" +
                        "long endTime = System.currentTimeMillis();\n" +
                        "System.out.println(\""+newName+"() cost:\" +(endTime - startTime));\n" +
                        "}";
                newMethod.setBody(bodyStr);// 替换新方法
                ctClass.addMethod(newMethod);// 增加新方法
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
```

#### Demo3.java

```java
public class Demo3 {
    
    /**
     * VM参数
     * -javaagent:D:\desktop\text\code\mycode\JavaAgentDemo\agent\target/agent.jar=input
     */
    public static void main(String[] args) throws Exception {
        sleep1();
        sleep2();
    }

    public static void sleep1(){
        ThreadUtil.sleep(1000);
    }

    public static void sleep2(){
        ThreadUtil.sleep(2000);
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
Class-Path: ../javassist-3.12.1.GA.jar
Premain-Class: cn.bigfire.Agent3
Can-Retransform-Classes: true

```

运行效果
```shell script
Agent3 premain :input
sleep1$Agent() cost:1005
sleep2$Agent() cost:2001
```

#### 测试Agent3
复制`MANIFEST.MF_Agent3`到`META-INF`目录下。去掉后缀`_Agent3`覆盖MANIFEST.MF文件

```shell script
cd 【项目目录】JavaAgentDemo\agent 
mvn clean package
```

![overwrite](https://gitee.com/dahuoyzs/res/raw/master/img/overwrite.png)

![mavenpackage](https://gitee.com/dahuoyzs/res/raw/master/img/mavenpackage.jpg)


启动demo3 设置JVM参数

`-javaagent:【项目目录】JavaAgentDemo\agent\target/agent.jar=input`


![image-20200731201651853](https://gitee.com/dahuoyzs/res/raw/master/img/image-20200731201651853.png)





