package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/5
 */
public class DateUtil {
    public String getDate(Date date){
        StringBuffer stringBuffer = new StringBuffer();
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH");
        SimpleDateFormat sdf4 = new SimpleDateFormat("mm");
        stringBuffer.append(sdf1.format(date)+"月");
        stringBuffer.append(sdf2.format(date)+"日 ");
        stringBuffer.append(sdf3.format(date)+":");
        stringBuffer.append(sdf4.format(date));
        return stringBuffer.toString();
    }
}