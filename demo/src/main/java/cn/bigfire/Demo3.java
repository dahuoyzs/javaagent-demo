package cn.bigfire;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/5/18  23:07
 * @ Desc   ：
 */

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
