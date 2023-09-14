package org.example;

import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/5
 */
public class CarRead {
    public List<Car> readExcel(String path, String strURL) {
        List<Car> carList = new ArrayList<>();
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
			......*/
            // 获取文件输入流
            InputStream inputStream = new FileInputStream(path);
            // 定义一个org.apache.poi.ss.usermodel.Workbook的变量
            Workbook workbook = null;
            // 截取路径名 . 后面的后缀名，判断是xls还是xlsx
            // 如果这个判断不对，就把equals换成 equalsIgnoreCase()
            if (path.substring(path.lastIndexOf(".") + 1).equals("xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (path.substring(path.lastIndexOf(" . " + 1)).equals("xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            }

            // 获取第一张表
            Sheet sheet = workbook.getSheetAt(0);
            // sheet.getPhysicalNumberOfRows()获取总的行数
            // 循环读取每一行
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                // 循环读取每一个格
                Row row = sheet.getRow(i);
                Car car = new Car();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Cell cell = row.getCell(1);
                cell.setCellType(CellType.STRING);
                String string = cell.getStringCellValue();
                car.setLifex(string);

                cell = row.getCell(2);
                cell.setCellType(CellType.STRING);
                string = cell.getStringCellValue();
                car.setArea(string);

                cell = row.getCell(3);
                cell.setCellType(CellType.STRING);
                string = cell.getStringCellValue();
                car.setCarNo(string);

                cell = row.getCell(4);
                cell.setCellType(CellType.STRING);
                string = cell.getStringCellValue();
                car.setPeopleType(string);

                cell = row.getCell(6);
                cell.setCellType(CellType.STRING);
                string = cell.getStringCellValue();
                if (!string.equals(" ")) {
                    car.setBookTime(simpleDateFormat.parse(string));
                }

                cell = row.getCell(7);
                cell.setCellType(CellType.STRING);
                string = cell.getStringCellValue();
                if (!string.equals(" ")) {
                    car.setSignTime(simpleDateFormat.parse(string));
                }

                cell = row.getCell(10);
                cell.setCellType(CellType.STRING);
                string = cell.getStringCellValue();
                car.setCarType(string);

                List<String> stringList = new ArrayList<>();

                cell = row.getCell(13);
                HSSFRichTextString richText = (HSSFRichTextString) cell.getRichStringCellValue();
                StringBuilder blackText = new StringBuilder();
                StringBuilder redText = new StringBuilder();
                int startPoint = 0;
                int endPoint = 0;
                for (endPoint = 0; endPoint < richText.numFormattingRuns(); endPoint++) {
                    int startIndex = richText.getIndexOfFormattingRun(endPoint);
                    int endIndex = endPoint < richText.numFormattingRuns() - 1 ? richText.getIndexOfFormattingRun(endPoint + 1) : richText.length();
                    short colorIndex = richText.getFontOfFormattingRun(endPoint);
                    String text = richText.getString().substring(startIndex, endIndex);
                    if (!text.equals(" → ")) {
                        stringList.add(text);
                    }
                    if (colorIndex == 6) {
                        startPoint = endPoint;
                        redText.append(text);
                    }
                }

                car.setRepositoryList(stringList);

                if (redText.length() > 0) {
                    car.setRout(redText.toString());
                    if (startPoint == endPoint - 1) {
                        car.setNum("0");
                    } else {
                        car.setNum(String.valueOf((int) Math.ceil((startPoint / 2) + 1)));
                    }
                }
                carList.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carList;


    }



}