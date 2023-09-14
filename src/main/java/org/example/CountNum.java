package org.example;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/11
 */

public class CountNum {
    @Test
    public void timeAvg() {
        CarRead carRead = new CarRead();
        OrderRead orderRead = new OrderRead();
        RepositoryOrderRead repositoryOrderRead = new RepositoryOrderRead();

        List<Order> orderList = orderRead.readExcel("E:\\Download\\拼车单时间节点2023-09-11.xls", null);
        List<RepositoryOrder> repositoryOrderList = repositoryOrderRead.readExcel("E:\\Download\\拼车单时间节点2023-09-11 (1).xls", null);

        Map<String, List<Count>> listMap = new HashMap<>();

        for (Order order : orderList) {
            if (order.getPeopleType().equals("重点客户")) {
                List<Count> countList = listMap.get(order.getSite());
                if (countList == null) {
                    countList = new ArrayList<>();
                    for (int i = 0; i < 4; ++i) {
                        Count count = new Count();
                        count.setTime(0l);
                        count.setCount(0);
                        countList.add(count);
                    }
                    listMap.put(order.getSite(), countList);
                }
                int num = 0;
                for (RepositoryOrder repositoryOrder : repositoryOrderList) {
                    if (repositoryOrder.getLifex().equals(order.getLifex())) {
                        ++num;
                    }
                }

                Count count = countList.get(num);
                if (order.getDepartureTime() != null && order.getSignTime() != null) {
                    count.setTime(count.getTime() + order.getDepartureTime().getTime() - order.getSignTime().getTime());
                    count.setCount(count.getCount() + 1);
                }
            }
        }
        for (String key : listMap.keySet()) { //遍历key
            System.out.println("key = " + key);
            List<Count> countList = listMap.get(key);
            for (Count count : countList) {
                if (count.getCount() != 0) {
                    Double day = (double) (count.getTime()) / (double) (60 * 60 * 1000) / (double) (count.getCount());
                    System.out.println(day);
                } else {
                    System.out.println(0);
                }
            }
        }
    }


}