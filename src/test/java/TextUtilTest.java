import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.example.HttpUtil;
import org.example.ReadExcel;
import org.example.TextUtil;
import org.json.JSONArray;
import org.junit.Test;
import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * @Description:
 * @Author: KinnakaIhou
 * @CreateTime: 2023/9/6
 */

public class TextUtilTest {
    @Test
    public void textUtil() {
        ReadExcel readExcel = new ReadExcel();
        readExcel.readExcel(null, "http://tms1.liby.com.cn/achievement/generateOrderTimeNodeDataNewExportExcel.do");
    }

    @Test
    public void httpText() {
        try {

            // 创建 GET 请求

            CloseableHttpClient httpClient = HttpClients.createDefault();

            // 创建 GET 请求
            HttpGet httpGet = new HttpGet("http://tms1.liby.com.cn/delivery/timeOrPlatformData.do?limit=10&offset=0&lifex=0095095304&setCarInfoType=1"); // 替换为您要发送请求的URL

            // 执行请求
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // 获取响应体数据
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            JSONObject json = new JSONObject(responseBody);
            // 获取 "total" 字段的值

            int total = json.getInt("total");

            // 获取 "rows" 字段的值，它是一个数组
            JSONArray rowsArray = json.getJSONArray("rows");

            // 遍历 "rows" 数组并提取数据
            for (int i = 0; i < rowsArray.length(); i++) {
                JSONObject rowObject = rowsArray.getJSONObject(i);
                String leaveTime = rowObject.getString("leaveTime");
                String reservoir = rowObject.getString("reservoir");
                // 在这里提取更多字段
                System.out.println("Leave Time: " + leaveTime);
                System.out.println("Reservoir: " + reservoir);
            }

            // 打印总数
            System.out.println("Total: " + total);

            // 打印响应数据
            System.out.println(responseBody);

            // 关闭响应
            response.close();

            // 关闭 HttpClient
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
@Test
    public void test2(){
        HttpUtil httpUtil = new HttpUtil();
    System.out.println(httpUtil.getRepositoryOrder("",""));
//        System.out.println(httpUtil.getCar());
    }

}