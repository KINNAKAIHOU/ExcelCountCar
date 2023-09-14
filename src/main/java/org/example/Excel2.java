package org.example;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/7
 */
public class Excel2 {
    @Test
    public void useExcel() {
        CarRead carRead = new CarRead();
        OrderRead orderRead = new OrderRead();
        RepositoryOrderRead repositoryOrderRead = new RepositoryOrderRead();

        List<String> endStrings = TextUtil.readFileCon("E:\\Download\\车牌.txt");//离场
        TextUtil.writeFileCon("E:\\Download\\车牌 - 副本.txt", endStrings);

        List<String> addStrings = Arrays.asList("00001232088");//人工加入

        List<Car> carList = carRead.readExcel("E:\\Download\\车辆进度看板2023-09-07.xls", null);
        List<Order> orderList = orderRead.readExcel("E:\\Download\\拼车单时间节点2023-09-07.xls", null);
        List<RepositoryOrder> repositoryOrderList = repositoryOrderRead.readExcel("E:\\Download\\拼车单时间节点2023-09-07 (1).xls", null);
        statisticians(carList, orderList, repositoryOrderList, addStrings, endStrings);
    }

    public void statisticians(List<Car> carList, List<Order> orderList, List<RepositoryOrder> repositoryOrderList,
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

                for (Order order : orderList) {
                    if (car.getLifex().equals(order.getLifex())) {

                        if (car.getBookTime() == null) {
                            if (order.getPickingTime() != null) {
                                System.out.println(++count + "）开始拣配时间是：" + dateUtil.getDate(order.getPickingTime()));
                            } else {
                                System.out.println("\033[31m" + (++count) + "）开始拣配时间是：(未开始拣配)\033[0m");
                            }

                            if (order.getAdmissionTime() != null) {
                                System.out.println(++count + "）车辆实际入场时间是：" + dateUtil.getDate(order.getAdmissionTime()));
                            }

                            if (car.getCarType().equals("作业中") ) {
                                if (order.getLoadingTime() != null) {
                                    System.out.println(++count + "）车辆开始装卸时间是：" + dateUtil.getDate(order.getLoadingTime()));
                                }
                            }

                            if (car.getCarType().equals("待离厂")) {
                                if (order.getLoadingTime() != null) {
                                    System.out.println(++count + "）车辆开始装卸时间是：" + dateUtil.getDate(order.getLoadingTime()));
                                }
                                if (order.getUnloadingTime() != null) {
                                    System.out.println(++count + "）车辆装卸结束时间是：" + dateUtil.getDate(order.getUnloadingTime()));
                                }
                            }

                            break;
                        } else {
                            //是否过了查询点
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
                                            System.out.println("\033[31m" + (++count) + "）" + repository + "开始拣配时间是：(未开始拣配)\033[0m");
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
                    }
                }
                System.out.println();
            }
        }

        //离场处理
        for (String lifex : endStrings) {
            for (Order order : orderList) {
                if (order.getLifex().equals(lifex)) {
                    System.out.print("您好！" + order.getCarNo() + "（" + order.getArea() + "）");
                    if (order.getBookTime() == null) {
                        System.out.println("没有提前预约，属于紧急订单，目前已离场。");
                    } else {
                        System.out.println("有提前预约，属于预约订单，目前已离场。");
                    }

                    System.out.println("具体节点如下：");

                    int count = 0;
                    if (order.getBookTime() == null) {
                        if (order.getPickingTime() != null) {
                            System.out.println(++count + "）开始拣配时间是：" + dateUtil.getDate(order.getPickingTime()));
                        } else {
                            System.out.println("\033[31m" + (++count) + "）开始拣配时间是：(未开始拣配)\033[0m");
                        }

                        if (order.getAdmissionTime() != null) {
                            System.out.println(++count + "）车辆实际入场时间是：" + dateUtil.getDate(order.getAdmissionTime()));
                        }

                        if (order.getLoadingTime() != null) {
                            System.out.println(++count + "）车辆开始装卸时间是：" + dateUtil.getDate(order.getLoadingTime()));
                        }

                        if (order.getUnloadingTime() != null) {
                            System.out.println(++count + "）车辆装卸结束时间是：" + dateUtil.getDate(order.getUnloadingTime()));
                        }

                        if (order.getDepartureTime() != null) {
                            System.out.println(++count + "）车辆实际离场时间是：" + dateUtil.getDate(order.getDepartureTime()));
                        }
                        break;
                    } else {
                        //是否过了查询点

                        //查找对应运输单号的同仓库
                        for (RepositoryOrder repositoryOrder : repositoryOrderList) {
                            if (order.getLifex().equals(repositoryOrder.getLifex())) {
                                if (repositoryOrder.getPickingTime() != null) {
                                    System.out.println(++count + "）" + repositoryOrder.getRepository() + "开始拣配时间是：" + dateUtil.getDate(repositoryOrder.getPickingTime()));
                                } else {
                                    System.out.println("\033[31m" + (++count) + "）" + repositoryOrder.getRepository() + "开始拣配时间是：(未开始拣配)\033[0m");
                                }

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
                                break;
                            }
                        }
                    }
                }


            }
        }
        TextUtil.writeFileCon("E:\\Download\\车牌.txt", newLifex);
    }
}