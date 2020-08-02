package cn.bigfire;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/5/18  23:07
 * @ Desc   ：
 */

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

