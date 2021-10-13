package com.lichongbing.ltools.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 3:07 下午
 * @description: TODO
 */
@Slf4j
@Configuration
public class HttpClientUtil {

    private static final ResponseHandler<JSONObject> responseHandler = new ResponseHandler<JSONObject>() {
        @Override
        public JSONObject handleResponse(final HttpResponse response) throws IOException {
            if (response.getStatusLine().getStatusCode() == 200) {
                return JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            } else {
                return null;
            }
        }
    };



    public static String sendPost(String strUrl, String requestParams) {
        return sendPost("",strUrl,requestParams);
    }

    /**
     * 登录调用-post请求
     * @param client
     * @param strUrl
     * @param requestParams
     * @return
     */
    public static String postByClient(CloseableHttpClient client, String strUrl, String requestParams){
        HttpPost post = new HttpPost(strUrl);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(requestParams,"UTF-8"));
        String data = null;
        try {
            JSONObject result = client.execute(post, responseHandler);
            data = result.toJSONString();
        } catch (Throwable e) {
            System.out.println("数据获取失败：" + e.getStackTrace());
        }
        return data;
    }


    /**
     * 向指定 URL 发送 POST请求
     *
     * @param strUrl        发送请求的 URL
     * @param requestParams 请求参数，格式 name1=value1&name2=value2
     * @return 远程资源的响应结果
     */
    public static String sendPost(String cookies,String strUrl, String requestParams) {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(strUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            // 设置接收数据的格式
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Cookie", cookies);
            // 设置发送数据的格式
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();    // 建立连接
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    httpURLConnection.getOutputStream(), "UTF-8");
            outputStreamWriter.append(requestParams);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            // 使用BufferedReader输入流来读取URL的响应
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String strLine = "";
            while ((strLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(strLine);
            }
            bufferedReader.close();
            String responseParams = stringBuffer.toString();

            return responseParams;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * 向指定 URL 发送 GET请求
     *
     * @param strUrl        发送请求的 URL
     * @param requestParams 请求参数
     * @return 远程资源的响应结果
     */
    public static String sendGet(String strUrl, String requestParams) {
        String responseBody = "";
        BufferedReader bufferedReader = null;
        try {
            String strRequestUrl = strUrl + "?" + requestParams;
            URL url = new URL(strRequestUrl);
            // 打开与 URL 之间的连接
            URLConnection urlConnection = url.openConnection();

            // 设置通用的请求属性
            urlConnection.setRequestProperty("accept", "*/*");
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            urlConnection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            urlConnection.connect();    // 建立连接
            // 获取所有响应头字段
            Map<String, List<String>> map = urlConnection.getHeaderFields();

            // 使用BufferedReader输入流来读取URL的响应
            bufferedReader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                responseBody += strLine;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return responseBody;
    }

    public String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        //得到请求的URL地址
        String requestUrl = request.getRequestURL().toString();
        //得到请求的URL地址中附带的参数
        String queryString = request.getQueryString();
        return ip;
    }

}

