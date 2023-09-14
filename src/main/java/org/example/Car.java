package org.example;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/5
 */
@Data
public class Car {
    private String lifex;
    private String area;
    private String carNo;
    private String peopleType;
    private Date bookTime;
    private Date signTime;
    private String carType;
    private String rout;
    private String num;
    private List<String> repositoryList;
}