package cn.bigfire;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.stream.Stream;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/5/18  23:12
 * @ Desc   ：
 */
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
