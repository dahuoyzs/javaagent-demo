package cn.bigfire;

import java.lang.instrument.Instrumentation;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/5/18  23:12
 * @ Desc   ：
 */
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
