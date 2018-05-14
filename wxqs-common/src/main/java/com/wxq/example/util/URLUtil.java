package com.wxq.example.util;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wangxueqing on 18/5/5.
 * URL工具类
 */
public class URLUtil {

    public static URL toURL(String path) throws MalformedURLException {
        try {
            return new URL(path);
        }
        catch (Exception e) {
            return new File(path).toURI().toURL();
        }
    }
    
    public static InputStream getURLResource(String urlPath) throws Exception{
    	//new一个URL对象    
        URL url = new URL(urlPath);    
        //打开链接    
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
        //设置请求方式为"GET"    
        conn.setRequestMethod("GET");    
        //超时响应时间为5秒    
        conn.setConnectTimeout(5 * 1000);    
        //通过输入流获取数据    
        InputStream inStream = conn.getInputStream(); 
        
        return inStream;
        
    }

}
