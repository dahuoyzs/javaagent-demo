package cn.bigfire;

import cn.bigfire.util.FileUtil;
import cn.hutool.core.util.StrUtil;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.security.ProtectionDomain;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/5/31  17:49
 * @ Desc   ：
 */
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
