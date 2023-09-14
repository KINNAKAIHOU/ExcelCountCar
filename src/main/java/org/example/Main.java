package org.example;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("判断是否施封");
        Excel3 excel = new Excel3();
        excel.useExcel();
    }


}