package com.robin.mynet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.robin.mynet.asynctask.LoadImgTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by robin on 2016/10/17.
 */
public class AsyncTaskAct extends AppCompatActivity{
    private static final int PICOK =1 ;
    private static final int PICFAIL =2 ;
    Button btn;
    ImageView image;

    class LoadImageTask extends AsyncTask<String,Void,Bitmap>{
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
            if(bitmap!=null){
                image.setImageBitmap(bitmap);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urlhandler_act);
        initView();
    }

    private void initView() {
        btn=(Button)findViewById(R.id.btnLoad);
        image=(ImageView)findViewById(R.id.imageView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startAsynTask1();
                startAsynTask2();
            }
        });
    }

    private void startAsynTask2() {
        new LoadImgTask(this, new LoadImgTask.CallbackListener() {
            @Override
            public void onResponse(Bitmap bitmap) {
                image.setImageBitmap(bitmap);
            }
        }).execute("http://192.168.7.244:8080/webdata2/images/hgmm.jpeg");


    }

    private void startAsynTask1() {
        new LoadImageTask().execute("http://192.168.7.244:8080/webdata2/images/hgmm.jpeg");
    }
}
