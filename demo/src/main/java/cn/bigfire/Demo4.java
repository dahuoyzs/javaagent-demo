package cn.bigfire;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/5/23  15:48
 * @ Desc   ：
 */
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
