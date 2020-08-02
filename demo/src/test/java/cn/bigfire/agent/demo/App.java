package cn.bigfire.agent.demo;

import cn.bigfire.LogLevelStarter;
import cn.hutool.core.util.StrUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/7/30  17:01
 * @ Desc   ：
 */
public class App {
    @Test
    public void testPath() throws Exception {

        String root = StrUtil.subBefore(System.getProperty("user.dir"), "\\", true);
        System.out.println(root);
    }

    @Test
    public void testFiled() throws Exception {
        Class logLevelStarterClass = LogLevelStarter.class;
        Field[] declaredFields = logLevelStarterClass.getDeclaredFields();
        Arrays.stream(declaredFields).forEach(System.out::println);
        Field isDebug = logLevelStarterClass.getDeclaredField("isDebug");
        boolean aBoolean = isDebug.getBoolean(logLevelStarterClass);
        System.out.println(aBoolean);
        isDebug.setBoolean(logLevelStarterClass,!aBoolean);
        System.out.println(isDebug.getBoolean(logLevelStarterClass));
        System.out.println("over");

    }


}
