##JavaAgent-agentmain方法1-实现运行时修改程序

### 效果：

实现运行时 修改程序 模拟项目中的动态日志 info <-> debug  

#### Agent5.java
```java
public class Agent5 {

    public static void premain(String agent, Instrumentation instrumentation){
        System.out.println("Agent5 premain 2param :" + agent);
        instrumentation.addTransformer(new StartTransformer(),true);

        //这个方式不行。因为启动时Class都还没有呢。
//        for (Class clazz:inst.getAllLoadedClasses()) {
//            if (clazz.getName().equals("cn.bigfire.LogLevelStarter")){
//                try {
//                    switchDebug(clazz);
//                    instrumentation.retransformClasses(clazz);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    public static void agentmain(String agent, Instrumentation instrumentation){
        System.out.println("Agent5 agentmain 2param :" + agent);
        for (Class clazz:instrumentation.getAllLoadedClasses()) {
            if (clazz.getName().equals("cn.bigfire.LogLevelStarter")){
                try {
                    switchAtomicDebug(clazz);
                    instrumentation.retransformClasses(clazz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class StartTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            //此时由于classBeingRedefined是空，所以还是不能用这个Class修改属性呢，只能通过 读取byte[]往堆里丢，才能用。
            if (className.equals("cn/bigfire/LogLevelStarter")){
                //【这是一个错误的思路】 premain的时候  classBeingRedefined是空的因为很多的Class还没加载到堆中
//                if (classBeingRedefined!=null){
//                    switchDebug(classBeingRedefined);
//                    return toBytes(classBeingRedefined);
//                }
                //正常的读取一共文件byte[]数组
                String root = StrUtil.subBefore(System.getProperty("user.dir"), "JavaAgentDemo", true);
                String classFile = root + "JavaAgentDemo/agent/src/main/resources/LogLevelStarter.class";
                return FileUtil.readBytes(classFile);
            }
            return classfileBuffer;
        }
    }

    /**
     * 可序列化对象转byte[]数组
     * @param clazz             要转byte[]数组的对象
     * @return byte[]           返回byte[]数组
     */
    public static byte[] toBytes(Serializable clazz){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(byteArrayOutputStream);
            stream.writeObject(clazz);
            return byteArrayOutputStream.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void switchDebug(Class clazz){
        try {
            Field field1 = clazz.getDeclaredField("isDebug");
            field1.setAccessible(true);
            boolean debug = field1.getBoolean(clazz);
            field1.setBoolean(clazz,!debug);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void switchAtomicDebug(Class clazz){
        try {
            Field field2 = clazz.getDeclaredField("atomicDebug");
            field2.setAccessible(true);
            AtomicBoolean atomicDebug = (AtomicBoolean)field2.get(clazz);
            atomicDebug.set(!atomicDebug.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
```

LogLevelStarter.java
```java
public class LogLevelStarter {

    public static volatile boolean isDebug = false;
    public static AtomicBoolean atomicDebug = new AtomicBoolean(false);

    /**
     * VM参数
     * -javaagent:D:\desktop\text\code\mycode\JavaAgentDemo\agent\target/agent.jar=input
     */
    public static void main(String[] args) throws Exception {
        new Thread(()->{
            for (;;){
                //死循环，每隔两秒打印一个日志。
                System.out.print(isDebug ? "volatile debug" : "volatile info");
                System.out.print("\t");
                System.out.println(atomicDebug.get() ? "atomicDebug debug" : "atomicDebug info");
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
Premain-Class: cn.bigfire.Agent5
Agent-Class: cn.bigfire.Agent5
Can-Retransform-Classes: true
Can-Redefine-Classes: true

```

此时的运行顺序
打包agent5 -> 先运行LogLevelStarter -> 运行demo4 ->选择程序LogLevelStarter结尾的程序，即可运行时修改文件



运行效果

LogLevelStarter
```shell script
Agent5 premain 2param :input
volatile debug	atomicDebug info
volatile debug	atomicDebug info
```

Demo4
```shell script
[0]ID:12592,Name:cn.bigfire.LogLevelStarter
[1]ID:12880,Name:cn.bigfire.Demo4
[2]ID:14832,Name:org.jetbrains.kotlin.daemon.KotlinCompileDaemon --daemon-runFilesPath xxx
[3]ID:14864,Name:
[4]ID:14852,Name:org.jetbrains.idea.maven.server.RemoteMavenServer36
[5]ID:8116,Name:org.jetbrains.jps.cmdline.Launcher xxx
请选择第几个
0
true
```

LogLevelStarter
```shell script
Agent5 premain 2param :input
volatile debug	atomicDebug info
volatile debug	atomicDebug info
Agent5 agentmain 2param :param
volatile debug	atomicDebug debug
volatile debug	atomicDebug debug
```


#### 测试Agent5
复制`MANIFEST.MF_Agent5`到`META-INF`目录下。去掉后缀`_Agent5`覆盖MANIFEST.MF文件

```shell script
cd 【项目目录】JavaAgentDemo\agent 
mvn clean package
```


![overwrite](https://gitee.com/dahuoyzs/res/raw/master/img/overwrite.png)

![mavenpackage](https://gitee.com/dahuoyzs/res/raw/master/img/mavenpackage.jpg)


启动LogLevelStarter 设置JVM参数

`-javaagent:【项目目录】JavaAgentDemo\agent\target/agent.jar=input`


![image-20200731201651853](https://gitee.com/dahuoyzs/res/raw/master/img/image-20200731201651853.png)


启动demo4 

选择程序LogLevelStarter结尾的程序，即可运行时修改文件.



