package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/7
 */
public class Excel3 {
    @Test
    public void useExcel() {
        CarRead carRead = new CarRead();
        OrderRead orderRead = new OrderRead();
        RepositoryOrderRead repositoryOrderRead = new RepositoryOrderRead();

        List<String> endStrings = TextUtil.readFileCon("E:\\Download\\车牌.txt");//离场
        TextUtil.writeFileCon("E:\\Download\\车牌 - 副本.txt", endStrings);

        //
        List<String> addStrings = Arrays.asList(" ");//人工加入

        List<Car> carList = carRead.readExcel("E:\\Download\\车辆进度看板2023-09-14.xls", null);
        List<RepositoryOrder> repositoryOrderList = repositoryOrderRead.readExcel("E:\\Download\\拼车单时间节点2023-09-14.xls", null);
        statisticians(carList, repositoryOrderList, addStrings, endStrings);
    }

    public void statisticians(List<Car> carList, List<RepositoryOrder> repositoryOrderList,
                              List<String> addStrings, List<String> endStrings) {
        //预备设置
        List<String> newLifex = new ArrayList<>();
        DateUtil dateUtil = new DateUtil();

        for (Car car : carList) {
            boolean flag = false;
            //查找新加入的运输单
            for (String addString : addStrings) {
                if (addString.equals(car.getLifex())) {
                    flag = true;
                    break;
                }
            }

            if (car.getPeopleType().equals("重点客户") || flag) {
                endStrings.remove(car.getLifex());
                newLifex.add(car.getLifex());

                System.out.print("您好！" + car.getCarNo() + "（" + car.getArea() + "）");
                if (car.getBookTime() == null) {
                    System.out.print("没有提前预约，属于紧急订单，");
                } else {
                    System.out.print("有提前预约，属于预约订单，");
                }

                if (car.getNum().equals("1")) {
                    System.out.println("目前正在第一个仓（" + car.getRout() + "）" + car.getCarType() + "。");
                } else if (car.getNum().equals("2")) {
                    System.out.println("目前正在第二个仓（" + car.getRout() + "）" + car.getCarType() + "。");
                } else if (car.getNum().equals("3")) {
                    System.out.println("目前正在第三个仓（" + car.getRout() + "）" + car.getCarType() + "。");
                } else if (car.getNum().equals("4")) {
                    System.out.println("目前正在第四个仓（" + car.getRout() + "）" + car.getCarType() + "。");
                } else if (car.getNum().equals("0")) {
                    System.out.println("目前正在最后一个仓（" + car.getRout() + "）" + car.getCarType() + "。");
                }

                System.out.println("具体节点如下：");

                int count = 0;
                System.out.print(++count + "）车间预约时间是：");
                if (car.getBookTime() == null) {
                    System.out.println("（无预约时间）");
                } else {
                    System.out.println(dateUtil.getDate(car.getBookTime()));
                }

                if (car.getSignTime() != null) {
                    System.out.println(++count + "）车辆实际签到时间是：" + dateUtil.getDate(car.getSignTime()));
                }

                if (car.getBookTime() == null) {
                    boolean check = false;
                    HttpUtil httpUtil = new HttpUtil();
                    JSONObject json = httpUtil.getByLifex(car.getLifex());

                    // 获取 "rows" 字段的值，它是一个数组
                    JSONArray rowsArray = json.getJSONArray("rows");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    // 遍历 "rows" 数组并提取数据
                    for (int i = 0; i < rowsArray.length(); i++) {

                        JSONObject rowObject = rowsArray.getJSONObject(i);
                        String region = rowObject.getString("region");
                        String admissionTime = rowObject.getString("enterTime");
                        String loadingTime = rowObject.getString("beginLoadTime");
                        String unloadingTime = rowObject.getString("finishLoadTime");
                        String departureTime = rowObject.getString("leaveTime");

                        for (RepositoryOrder repositoryOrder : repositoryOrderList) {
                            if (car.getLifex().equals(repositoryOrder.getLifex()) && repositoryOrder.getRepository().equals(region)) {
                                if (repositoryOrder.getPickingTime() != null) {
                                    System.out.println(++count + "）" + region + "开始拣配时间是：" + dateUtil.getDate(repositoryOrder.getPickingTime()));
                                } else {
                                    if(!region.equals("立体仓")){
                                        System.out.println("\033[31m" + (++count) + "）" + region + "开始拣配时间是：(未开始拣配)\033[0m");
                                    }
                                }
                            }
                        }
                        try {
                            if (!admissionTime.equals("")) {
                                System.out.println(++count + "）" + region + "车辆实际入场时间是：" + dateUtil.getDate(simpleDateFormat.parse(admissionTime)));
                            }

                            if (!loadingTime.equals("")) {
                                System.out.println(++count + "）" + region + "车辆开始装卸时间是：" + dateUtil.getDate(simpleDateFormat.parse(loadingTime)));
                            }

                            if (!unloadingTime.equals("")) {
                                System.out.println(++count + "）" + region + "车辆装卸结束时间是：" + dateUtil.getDate(simpleDateFormat.parse(unloadingTime)));
                            }

                            if (!departureTime.equals("")) {
                                System.out.println(++count + "）" + region + "车辆实际离场时间是：" + dateUtil.getDate(simpleDateFormat.parse(departureTime)));
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if (car.getRout().equals(region)) {
                            break;
                        }
                    }
                } else {
                    boolean check = false;
                    for (String repository : car.getRepositoryList()) {

                        if (check == true) {
                            break;
                        }
                        if (repository.equals(car.getRout())) {
                            check = true;
                        }

                        //查找对应运输单号的同仓库
                        for (RepositoryOrder repositoryOrder : repositoryOrderList) {
                            if (car.getLifex().equals(repositoryOrder.getLifex()) && repositoryOrder.getRepository().equals(repository)) {
                                if (repositoryOrder.getPickingTime() != null) {
                                    System.out.println(++count + "）" + repository + "开始拣配时间是：" + dateUtil.getDate(repositoryOrder.getPickingTime()));
                                } else {
                                    if(!repository.equals("立体仓")){
                                        System.out.println("\033[31m" + (++count) + "）" + repository + "开始拣配时间是：(未开始拣配)\033[0m");
                                    }
                                }

                                if (repositoryOrder.getAdmissionTime() != null) {
                                    System.out.println(++count + "）" + repository + "车辆实际入场时间是：" + dateUtil.getDate(repositoryOrder.getAdmissionTime()));
                                }

                                if (repositoryOrder.getLoadingTime() != null) {
                                    System.out.println(++count + "）" + repository + "车辆开始装卸时间是：" + dateUtil.getDate(repositoryOrder.getLoadingTime()));
                                }

                                if (repositoryOrder.getUnloadingTime() != null) {
                                    System.out.println(++count + "）" + repository + "车辆装卸结束时间是：" + dateUtil.getDate(repositoryOrder.getUnloadingTime()));
                                }

                                if (repositoryOrder.getDepartureTime() != null) {
                                    System.out.println(++count + "）" + repository + "车辆实际离场时间是：" + dateUtil.getDate(repositoryOrder.getDepartureTime()));
                                }
                                break;
                            }
                        }
                    }
                }
                System.out.println();
            }
        }

        //离场处理
        for (String lifex : endStrings) {

            for (RepositoryOrder repository : repositoryOrderList) {
                if (!repository.getLifex().equals(lifex)) {
                    continue;
                }

                System.out.print("您好！" + repository.getCarNo() + "（" + repository.getArea() + "）");
                if (repository.getBookTime() == null) {
                    System.out.println("没有提前预约，属于紧急订单，目前已离场。");
                } else {
                    System.out.println("有提前预约，属于预约订单，目前已离场。");
                }

                System.out.println("具体节点如下：");


                int count = 0;

                System.out.print(++count + "）车间预约时间是：");
                if (repository.getBookTime() == null) {
                    System.out.println("（无预约时间）");
                } else {
                    System.out.println(dateUtil.getDate(repository.getBookTime()));
                }

                if (repository.getSignTime() != null) {
                    System.out.println(++count + "）车辆实际签到时间是：" + dateUtil.getDate(repository.getSignTime()));
                }

                if (repository.getBookTime() == null) {

                    HttpUtil httpUtil = new HttpUtil();
                    JSONObject json = httpUtil.getByLifex(lifex);

                    // 获取 "rows" 字段的值，它是一个数组
                    JSONArray rowsArray = json.getJSONArray("rows");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    // 遍历 "rows" 数组并提取数据
                    for (int i = 0; i < rowsArray.length(); i++) {
                        JSONObject rowObject = rowsArray.getJSONObject(i);
                        String region = rowObject.getString("region");
                        String admissionTime = rowObject.getString("enterTime");
                        String loadingTime = rowObject.getString("beginLoadTime");
                        String unloadingTime = rowObject.getString("finishLoadTime");
                        String departureTime = rowObject.getString("leaveTime");

                        for (RepositoryOrder repositoryOrder : repositoryOrderList) {
                            if (lifex.equals(repositoryOrder.getLifex()) && region.equals(repositoryOrder.getRepository())) {
                                if (repositoryOrder.getPickingTime() != null) {
                                    System.out.println(++count + "）" + region + "开始拣配时间是：" + dateUtil.getDate(repositoryOrder.getPickingTime()));
                                } else {
                                    if(!region.equals("立体仓")){
                                        System.out.println("\033[31m" + (++count) + "）" + region + "开始拣配时间是：(未开始拣配)\033[0m");
                                    }
                                }
                            }
                        }
                        try {
                            if (!admissionTime.equals("")) {
                                System.out.println(++count + "）" + region + "车辆实际入场时间是：" + dateUtil.getDate(simpleDateFormat.parse(admissionTime)));
                            }

                            if (!loadingTime.equals("")) {
                                System.out.println(++count + "）" + region + "车辆开始装卸时间是：" + dateUtil.getDate(simpleDateFormat.parse(loadingTime)));
                            }

                            if (!unloadingTime.equals("")) {
                                System.out.println(++count + "）" + region + "车辆装卸结束时间是：" + dateUtil.getDate(simpleDateFormat.parse(unloadingTime)));
                            }

                            if (!departureTime.equals("")) {
                                System.out.println(++count + "）" + region + "车辆实际离场时间是：" + dateUtil.getDate(simpleDateFormat.parse(departureTime)));
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    for (RepositoryOrder repositoryOrder : repositoryOrderList) {
                        if (lifex.equals(repositoryOrder.getLifex())) {
                            if (repositoryOrder.getPickingTime() != null) {
                                System.out.println(++count + "）" + repositoryOrder.getRepository() + "开始拣配时间是：" + dateUtil.getDate(repositoryOrder.getPickingTime()));
                            } else {
                                if(!repositoryOrder.getRepository().equals("立体仓")){
                                    System.out.println("\033[31m" + (++count) + "）" + repositoryOrder.getRepository() + "开始拣配时间是：(未开始拣配)\033[0m");
                                }          }

                            if (repositoryOrder.getAdmissionTime() != null) {
                                System.out.println(++count + "）" + repositoryOrder.getRepository() + "车辆实际入场时间是：" + dateUtil.getDate(repositoryOrder.getAdmissionTime()));
                            }

                            if (repositoryOrder.getLoadingTime() != null) {
                                System.out.println(++count + "）" + repositoryOrder.getRepository() + "车辆开始装卸时间是：" + dateUtil.getDate(repositoryOrder.getLoadingTime()));
                            }

                            if (repositoryOrder.getUnloadingTime() != null) {
                                System.out.println(++count + "）" + repositoryOrder.getRepository() + "车辆装卸结束时间是：" + dateUtil.getDate(repositoryOrder.getUnloadingTime()));
                            }

                            if (repositoryOrder.getDepartureTime() != null) {
                                System.out.println(++count + "）" + repositoryOrder.getRepository() + "车辆实际离场时间是：" + dateUtil.getDate(repositoryOrder.getDepartureTime()));
                            }
                        }
                    }
                }
                System.out.println();
                break;
            }
        }
        TextUtil.writeFileCon("E:\\Download\\车牌.txt", newLifex);
    }
}