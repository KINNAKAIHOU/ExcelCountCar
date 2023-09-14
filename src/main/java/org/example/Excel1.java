package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/7
 */
public class Excel1 {
    public void useExcel(){
        CarRead carRead = new CarRead();
        OrderRead orderRead = new OrderRead();
        List<String> endStrings = TextUtil.readFileCon("E:\\Download\\车牌.txt");//离场
        TextUtil.writeFileCon("E:\\Download\\车牌 - 副本.txt", endStrings);
        List<String> addStrings = Arrays.asList("闽DH8893");//人工加入

        List<Car> carList = carRead.readExcel("E:\\Download\\车辆进度看板2023-09-07.xls", null);
        List<Order> orderList = orderRead.readExcel("E:\\Download\\拼车单时间节点2023-09-07.xls", null);

        statisticians(carList, orderList, addStrings, endStrings);
    }

    public  void statisticians(List<Car> carList, List<Order> orderList, List<String> addStrings, List<String> endStrings) {
        List<String> newCarNo = new ArrayList<>();
        DateUtil dateUtil = new DateUtil();
        for (Car car : carList) {
            boolean flag = false;
            for (String addString : addStrings) {
                if (addString.equals(car.getCarNo())) {
                    flag = true;
                }
            }
            if (car.getPeopleType().equals("重点客户") || flag) {
                endStrings.remove(car.getCarNo());
                newCarNo.add(car.getCarNo());
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

                System.out.print("1）车间预约时间是：");
                if (car.getBookTime() == null) {
                    System.out.println("（无预约时间）");
                } else {
                    System.out.println(dateUtil.getDate(car.getBookTime()));
                }

                if (car.getSignTime() != null) {
                    System.out.println("2）车辆实际签到时间是：" + dateUtil.getDate(car.getSignTime()));
                }

                for (Order order : orderList) {
                    if (car.getCarNo().equals(order.getCarNo()) && car.getLifex().equals(order.getLifex())) {

                        if (order.getPickingTime() != null) {
                            System.out.println("3）开始拣配时间是：" + dateUtil.getDate(order.getPickingTime()));
                        } else {
                            System.out.println("\033[31m3）开始拣配时间是：(未开始拣配)\033[0m");
                        }

                        if (order.getAdmissionTime() != null) {
                            System.out.println("4）车辆实际入场时间是：" + dateUtil.getDate(order.getAdmissionTime()));
                        }

                        if (car.getCarType().equals("作业中") && order.getDepartureTime() == null) {
                            if (order.getLoadingTime() != null) {
                                System.out.println("5）车辆当前开始装卸时间是：" + dateUtil.getDate(order.getLoadingTime()));
                            }
                        }

                        if (car.getCarType().equals("待离厂")) {
                            if (order.getLoadingTime() != null) {
                                System.out.println("6）车辆当前开始装卸时间是：" + dateUtil.getDate(order.getLoadingTime()));
                            }
                            if (order.getUnloadingTime() != null) {
                                System.out.println("6）车辆当前装卸结束时间是：" + dateUtil.getDate(order.getUnloadingTime()));
                            }
                        }

                        break;
                    }
                }
                System.out.println();
            }
        }

        //离场处理
        for (String string : endStrings) {
            for (Order order : orderList) {
                if (order.getCarNo().equals(string)) {
                    System.out.print("您好！" + string + "（" + order.getArea() + "）");
                    if (order.getBookTime() == null) {
                        System.out.println("没有提前预约，属于紧急订单，目前已离场。");
                    } else {
                        System.out.println("有提前预约，属于预约订单，目前已离场。");
                    }

                    System.out.println("具体节点如下：");

                    System.out.print("1）车间预约时间是：");
                    if (order.getBookTime() == null) {
                        System.out.println("（无预约时间）");
                    } else {
                        System.out.println(dateUtil.getDate(order.getBookTime()));
                    }

                    if (order.getSignTime() != null) {
                        System.out.println("2）车辆实际签到时间是：" + dateUtil.getDate(order.getSignTime()));
                    }

                    if (order.getPickingTime() != null) {
                        System.out.println("3）开始拣配时间是：" + dateUtil.getDate(order.getPickingTime()));
                    } else {
                        System.out.println("\033[31m3）开始拣配时间是：(未开始拣配)\033[0m");
                    }

                    if (order.getAdmissionTime() != null) {
                        System.out.println("4）车辆实际入场时间是：" + dateUtil.getDate(order.getAdmissionTime()));
                    }

                    if (order.getUnloadingTime() != null) {
                        System.out.println("5）车辆当前装卸结束时间是：" + dateUtil.getDate(order.getUnloadingTime()));
                    }

                    if (order.getDepartureTime() != null) {
                        System.out.println("6）车辆实际离场时间是：" + dateUtil.getDate(order.getDepartureTime()));
                    }

                    System.out.println();
                    break;
                }
            }
        }

        TextUtil.writeFileCon("E:\\Download\\车牌.txt", newCarNo);
    }
}