## JavaAgent-agentmain方法1-实现运行时修改程序

### 效果：

实现运行时 修改程序 hello -> hello agented



#### Agent4.java
```java
public class Agent4 {

    public static void premain(String agent){
        System.out.println("Agent4 premain 1param:" + agent);
    }

    public static void premain(String agent, Instrumentation instrumentation) {
        System.out.println("Agent4 premain 2param:" + agent);
        //premain时，由于堆里还没有相应的Class。所以直接addTransformer,程序就会生效。
//        instrumentation.addTransformer(new ConsoleTransformer(),true);
    }

    public static void agentmain(String agent, Instrumentation instrumentation){
        System.out.println("Agent4 agentmain 2param :" + agent);
        instrumentation.addTransformer(new ConsoleTransformer(),true);
        //agentmain运行时 由于堆里已经存在Class文件，所以新添加Transformer后
        // 还要再调用一个  inst.retransformClasses(clazz); 方法来更新Class文件
        for (Class clazz:instrumentation.getAllLoadedClasses()) {
            if (clazz.getName().contains("cn.bigfire.Console")){
                try {
                    instrumentation.retransformClasses(clazz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void agentmain(String agent){
        System.out.println("Agent4 agentmain 1param :" + agent);
    }

}
```

Demo4
```java
public class Demo4 {
    /**
     * 打包agent4 -> 先运行demo2 -> 运行demo4 ->选择程序demo2结尾的程序，即可运行时修改文件
     * VM参数
     * -javaagent:D:\desktop\text\code\mycode\JavaAgentDemo\agent\target/agent.jar=input
     * */
    public static void main(String[] args) throws Exception {
        while (true){
            List<VirtualMachineDescriptor> list = VirtualMachine.list();
            for (int i = 0; i < list.size(); i++) {
                VirtualMachineDescriptor jvm = list.get(i);;
                System.out.println("[" +i+ "]ID:"+jvm.id()+",Name:"+jvm.displayName());
            }
            System.out.println("请选择第几个");
            Scanner scanner = new Scanner(System.in);
            int s = scanner.nextInt();
            VirtualMachineDescriptor virtualMachineDescriptor = list.get(s);
            VirtualMachine attach = VirtualMachine.attach(virtualMachineDescriptor.id());
            String root = StrUtil.subBefore(System.getProperty("user.dir"), "JavaAgentDemo", true);
            String agentJar = root + "JavaAgentDemo\\agent\\target\\agent.jar";
            File file = new File(agentJar);
            System.out.println(file.exists());
            attach.loadAgent(agentJar,"param");
            attach.detach();
        }
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
Premain-Class: cn.bigfire.Agent4
Agent-Class: cn.bigfire.Agent4
Can-Retransform-Classes: true
Can-Redefine-Classes: true

```

此时的运行顺序
打包agent4 -> 先运行demo2 -> 运行demo4 ->选择程序demo2结尾的程序，即可运行时修改文件


运行效果
Demo2
```shell script
Agent4 premain 2param:input
hello
hello
```

Demo4
```shell script
[0]ID:12480,Name:cn.bigfire.Demo2
[1]ID:14832,Name:org.jetbrains.kotlin.daemon.KotlinCompileDaemon --daemon-runFilesPath xxx
[2]ID:14864,Name:
[3]ID:3952,Name:cn.bigfire.Demo4
[4]ID:14852,Name:org.jetbrains.idea.maven.server.RemoteMavenServer36
[5]ID:11928,Name:org.jetbrains.jps.cmdline.Launcher xxx
请选择第几个
0
true
```

Demo2
```shell script
Agent4 premain 2param:input
hello
hello
Agent4 agentmain 2param :param
hello  agented
hello  agented
hello  agented
```




#### 测试Agent4
复制`MANIFEST.MF_Agent4`到`META-INF`目录下。去掉后缀`_Agent4`覆盖MANIFEST.MF文件

```shell script
cd 【项目目录】JavaAgentDemo\agent 
mvn clean package
```

![overwrite](https://gitee.com/dahuoyzs/res/raw/master/img/overwrite.png)

![mavenpackage](https://gitee.com/dahuoyzs/res/raw/master/img/mavenpackage.jpg)


启动demo2 设置JVM参数

`-javaagent:【项目目录】JavaAgentDemo\agent\target/agent.jar=input`

![image-20200731201651853](https://gitee.com/dahuoyzs/res/raw/master/img/image-20200731201651853.png)


启动demo4 

选择程序demo2结尾的程序，即可运行时修改文件.

