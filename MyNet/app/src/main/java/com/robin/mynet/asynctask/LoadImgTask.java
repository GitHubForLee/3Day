package com.robin.mynet.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by robin on 2016/10/17.
 */
public class LoadImgTask extends AsyncTask<String,Void,Bitmap> {

    private Context context;
    private CallbackListener callback;
    public LoadImgTask(Context context,CallbackListener callback){
        this.context=context;
        this.callback=callback;

    }
    @Override
    protected Bitmap doInBackground(String... params) {
        //1.创建url
        URL url= null;
        InputStream is=null;
        HttpURLConnection conn=null;
        try {
            url = new URL(params[0]);
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
            if(responseCode==HttpURLConnection.HTTP_OK){//代表访问web资源成功
                //5.获取IO流
                is=conn.getInputStream();
                Bitmap bitmap= BitmapFactory.decodeStream(is);
                return bitmap;
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

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap!=null&&callback!=null){
            callback.onResponse(bitmap);
        }


    }

    public static interface CallbackListener{
        public void onResponse(Bitmap bitmap);
    };



}
