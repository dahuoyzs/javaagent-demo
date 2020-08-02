package cn.bigfire.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuoyzs
 * @ Date   ：2020/7/31  10:34
 * @ Desc   ：
 */
public class FileUtil {
    public static byte[] readBytes(String fileName){
        try{
            File file = new File(fileName);
            if (file.exists()&&file.isFile()){
                return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
