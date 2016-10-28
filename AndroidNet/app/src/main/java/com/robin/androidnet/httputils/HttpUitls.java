package com.robin.androidnet.httputils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by robin on 2016/8/10.
 */
public class HttpUitls {

    //根据url加载web网络数据生成byte[]
    public static byte[] loadDataForHttp(String dataUrl){
        try {
            //1.创建一个url
            URL url=new URL(dataUrl);//url
            //2.创建httpurlconnection
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            //3.设置属性
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            //设置超时连接
            httpURLConnection.setConnectTimeout(5000);
            //4.开始连接
            httpURLConnection.connect();
            //5 获取响应码
            int respondCode= httpURLConnection.getResponseCode();
            //字节数组输出流接收网络输入流 才能转换字节数组
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            if(respondCode==HttpURLConnection.HTTP_OK) {
                InputStream is=httpURLConnection.getInputStream();
                byte[] buffer=new byte[512];
                int num=-1;
                while ((num=is.read(buffer))!=-1){
                    bos.write(buffer,0,num);
                    bos.flush();
                }

                return  bos.toByteArray();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream LoadStreamForHttp(String dataUrl){
        try {
            //1.创建一个url
            URL url=new URL(dataUrl);//url
            //2.创建httpurlconnection
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            //3.设置属性
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            //设置超时连接
            httpURLConnection.setConnectTimeout(5000);
            //4.开始连接
            httpURLConnection.connect();
            //5 获取响应码
            int respondCode= httpURLConnection.getResponseCode();

            if(respondCode==HttpURLConnection.HTTP_OK) {
                InputStream is=httpURLConnection.getInputStream();


                return  is;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
