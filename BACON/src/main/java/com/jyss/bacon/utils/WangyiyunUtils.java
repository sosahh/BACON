package com.jyss.bacon.utils;
import com.jyss.bacon.entity.WangyiyunEntity.Info;
import com.jyss.bacon.entity.WangyiyunEntity.ResultJson;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSON;

import java.util.*;


public class WangyiyunUtils {
    public static Map<String ,String> signWangyiyun(String  account,String  nick,String  headpic) {
        HashMap<String, String> m = new HashMap<>();
        m.put("code","");
        m.put("token","");
        // DefaultHttpClient httpClient = new DefaultHttpClient();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "https://api.netease.im/nimserver/user/create.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "2b78f3dc03fac98e0e574d590ef837e6";
        String appSecret = "12bf6e4f6e12";
        String nonce = CommTool.getNonceStr(10);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", account));
        nvps.add(new BasicNameValuePair("name", nick));
        nvps.add(new BasicNameValuePair("icon", headpic));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String  result  =EntityUtils.toString(response.getEntity(), "utf-8");
            // 打印执行结果
            System.out.println(result);
            ResultJson rj = JSON.parseObject(result, ResultJson.class);
         if (rj!=null){
             //200成功
             m.put("code",rj.getCode());
             Info info = rj.getInfo();
             if (info!=null){
                 m.put("token",info.getToken());
             }
         }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return m;
    }

    public static String updateWangyiyun(String  account,String  nick,String  headpic) {

        // DefaultHttpClient httpClient = new DefaultHttpClient();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "https://api.netease.im/nimserver/user/updateUinfo.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "2b78f3dc03fac98e0e574d590ef837e6";
        String appSecret = "12bf6e4f6e12";
        String nonce = CommTool.getNonceStr(10);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", account));
        nvps.add(new BasicNameValuePair("name", nick));
        nvps.add(new BasicNameValuePair("icon", headpic));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String  result  =EntityUtils.toString(response.getEntity(), "utf-8");
            // 打印执行结果
            System.out.println(result);
            ResultJson rj = JSON.parseObject(result, ResultJson.class);
            if (rj!=null){
                //200成功
                return rj.getCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "414";
    }
    public static void main(String[] args) {
       ///System.out.print(signWangyiyun("12345222566"));
       //System.out.print(updateWangyiyun("223800002","abcd",null));
    }

}
