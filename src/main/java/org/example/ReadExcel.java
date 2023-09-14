package org.example;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/5
 */
public class ReadExcel {
    public void readExcel(String path,String strURL) {
        try {
			/*// 如果需要通过URL获取资源的加上以下的代码，不需要的省略就行
			URL url = new URL(strURL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			// 设置超时间为3秒
			conn.setConnectTimeout(3*1000);
			// 防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 获取输入流
			InputStream inputStream = conn.getInputStream();
			Workbook workbook  = Workbook.getWorkbook(inputStream);
			......*/

            // 解析路径的file文件
            Workbook workbook = Workbook.getWorkbook(new File(path));
            // 获取第一张工作表
            Sheet sheet = workbook.getSheet(0);
            // 循环获取每一行数据 因为默认第一行为标题行，我们可以从 1 开始循环，如果需要读取标题行，从 0 开始
            // sheet.getRows() 获取总行数
            for (int i = 1; i < sheet.getRows(); i++) {
                // 获取第一列的第 i 行信息 sheet.getCell(列，行)，下标从0开始
                for (int j = 0; j<sheet.getColumns(); j++){
                    String id = sheet.getCell(j, i).getContents();
                    // 获取第二列的第 i 行信息
                    System.out.print(id + " ");
                    // 存入本地或者是存入对象等根据给人需求自己定就行,创建对象存储，然后加入集合中
                    // ......
                }
                System.out.println(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

}