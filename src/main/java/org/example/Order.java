package org.example;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/5
 */
@Data
public class Order {
    private String lifex;
    private String area;
    private String carNo;
    private String site;
    private String peopleType;
    private Date bookTime;
    private Date pickingTime;
    private Date signTime;
    private Date admissionTime;
    private Date loadingTime;
    private Date unloadingTime;
    private Date departureTime;
}