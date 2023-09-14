package org.example;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/7
 */
@Data
public class RepositoryOrder {
    private String lifex;
    private String area;
    private String carNo;
    private String repository;
    private Date bookTime;
    private Date pickingTime;
    private Date signTime;
    private Date admissionTime;
    private Date loadingTime;
    private Date unloadingTime;
    private Date departureTime;
}