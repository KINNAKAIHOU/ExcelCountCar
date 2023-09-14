package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/7
 */
public class HttpUtil {
    public JSONObject getByLifex(String lifex) {
        JSONObject json = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://tms1.liby.com.cn/delivery/timeOrPlatformData.do?limit=10&offset=0&" +
                    "lifex=" + lifex + "&setCarInfoType=1"); // 替换为您要发送请求的URL
            CloseableHttpResponse response = httpClient.execute(httpGet);


            // 获取响应体数据
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            json = new JSONObject(responseBody);
            // 关闭响应
            response.close();

            // 关闭 HttpClient
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getCar() {
        JSONObject json = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://tms1.liby.com.cn/board/carLoadLineBoardLoadSequenceDataNoeNew.do" +
                    "?siteId=101&pageIndex=1&pageSize=1000"); // 替换为您要发送请求的URL
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // 获取响应体数据
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            json = new JSONObject(responseBody);
            // 关闭响应
            response.close();

            // 关闭 HttpClient
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getRepositoryOrder(String startTime, String endTime) {
        JSONObject json = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(
                    "http://tms1.liby.com.cn/achievement/orderTimeNodeData.do?limit=10&offset=0&" +
                    "order=asc&sort=true&query=&queryType=lifex3&bezei=&carno=&startBook=&endBook=&" +
                    "startSet&endSet&startWork=&endWork=&startSignIn=&endSignIn=&" +
                    "orStartSignIn=&orEndSignIn=&showManualOrder=false&startRelease=&endRelease=&urgentStatus=&" +
                    "matnr=&kunna=&bezeiNull=0&customerType=&orderType=&lfart=&kunnr=&taskStatus=&siteIds=101&" +
                    "queryType1=lifex&lifexs=H0095108655%2C"); // 替换为您要发送请求的URL
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // 获取响应体数据
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            json = new JSONObject(responseBody);
            // 关闭响应
            response.close();

            // 关闭 HttpClient
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


}