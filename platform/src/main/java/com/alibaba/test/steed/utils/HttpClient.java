package com.alibaba.test.steed.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by liyang on 2019/8/29.
 */
public class HttpClient {

    public static String get(String url){
        String responsestr = "";
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet( url );

            CloseableHttpResponse response = httpclient.execute( httpget );
            responsestr = EntityUtils.toString( response.getEntity(), "UTF-8" );
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return responsestr;
    }

    public static String post(String url,String msg){
        String responsestr = "";
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost( url );
            StringEntity requestEntity = new StringEntity(msg,"UTF-8");
            requestEntity.setContentEncoding("UTF-8");
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(requestEntity);

            CloseableHttpResponse response = httpclient.execute( httppost );
            responsestr = EntityUtils.toString( response.getEntity(), "UTF-8" );
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return responsestr;
    }
}
