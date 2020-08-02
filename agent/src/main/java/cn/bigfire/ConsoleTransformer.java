package cn.bigfire;

import cn.bigfire.util.FileUtil;
import cn.hutool.core.util.StrUtil;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/5/23  19:12
 * @ Desc   ：
 */
public class ConsoleTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals("cn/bigfire/Console")){
            String root = StrUtil.subBefore(System.getProperty("user.dir"), "JavaAgentDemo", true);
            String classFile = root + "JavaAgentDemo/agent/src/main/resources/Console.class";
            return FileUtil.readBytes(classFile);
        }
        return classfileBuffer;
    }
}
