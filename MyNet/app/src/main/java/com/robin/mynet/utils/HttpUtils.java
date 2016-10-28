package com.robin.mynet.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by robin on 2016/10/17.
 */
public class HttpUtils {
    //根据url加载web数据生成byte[]的静态方法
    public static byte[] loadDataForHttp(String dataUrl){
        //1.创建url
        URL url= null;
        InputStream is=null;
        HttpURLConnection conn=null;
        try {
            url = new URL(dataUrl);
            //2.创建连接对象
            conn= (HttpURLConnection) url.openConnection();
            //3.处理设置参数和一般请求属性
            conn.setDoInput(true);
            conn.setRequestMethod("GET");//默认GET请求
            conn.setConnectTimeout(5000);
            //4.开始连接
            conn.connect();

            //5.获取响应码
            int responseCode=conn.getResponseCode();

            //字节数组输出流接收网络的输入流 就能转换字节数组
            ByteArrayOutputStream bos=new ByteArrayOutputStream();

            if(responseCode==HttpURLConnection.HTTP_OK){//代表访问web资源成功
                //5.获取IO流
                is=conn.getInputStream();
                byte[] buffer=new byte[1024];
                int num=-1;
                while((num=is.read(buffer))!=-1){
                    bos.write(buffer,0,num);
                    bos.flush();
                }
                return bos.toByteArray();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;

    }
}
