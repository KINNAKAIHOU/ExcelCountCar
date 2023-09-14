package org.example;

import org.junit.Test;
import org.w3c.dom.ls.LSException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/6
 */


public class TextUtil {
    // 读取文件内容  参数要完成路径和文件名 String filePathName="D:/test/tgj/test1.txt";
    public static List<String> readFileCon(String filePathName){
        List<String> strList = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePathName));
            String line = reader.readLine();
            while (line != null) {
                strList.add(line);
                line = reader.readLine();// 继续读取下一行
            }
            reader.close();
            return strList;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件不存在");
            return strList;
        }
    }

    // 给指定文件写入内容。若没有就创建，但不能创建目录。String filePathName="D:/test/tgj/test2.txt";
    public static void writeFileCon(String filePathName, List<String> str){
        try (FileWriter fw = new FileWriter(filePathName);
             BufferedWriter info = new BufferedWriter(fw))
        {
            for (String s : str) {
                info.write(String.format(s + "%n")); // 加个 %n 相当于换行
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入失败");
        }
    }

}

