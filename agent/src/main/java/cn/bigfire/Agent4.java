package cn.bigfire;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/5/23  15:48
 * @ Desc   ：
 */
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
