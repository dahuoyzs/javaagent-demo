package cn.bigfire;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/5/18  23:12
 * @ Desc   ：
 */
public class Agent1 {

    /**
     * 可以运行在main方法启动前
     * @param agent             输入的参数
     */
    public static void premain(String agent){
        System.out.println("Agent1 premain :" + agent);
    }

}
