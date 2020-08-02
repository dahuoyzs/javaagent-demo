package cn.bigfire;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/5/31  17:50
 * @ Desc   ：
 */
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
