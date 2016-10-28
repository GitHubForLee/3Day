package com.robin.mynet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by robin on 2016/10/17.
 */
public class UrlHandlerAct extends AppCompatActivity{
    private static final int PICOK =1 ;
    private static final int PICFAIL =2 ;
    Button btn;
    ImageView image;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PICOK:
                    Bitmap bitmap= (Bitmap) msg.obj;
                    image.setImageBitmap(bitmap);
                    break;
                case PICFAIL:
                    Toast.makeText(UrlHandlerAct.this, "加载失败!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urlhandler_act);
        btn=(Button)findViewById(R.id.btnLoad);
        image=(ImageView)findViewById(R.id.imageView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAsynTask();
                startAsynTask2();
            }
        });
    }

    private void startAsynTask2() {




    }

    private void startAsynTask() {
        new Thread(){
            @Override
            public void run() {
                //1.创建url
                URL url= null;
                InputStream is=null;
                HttpURLConnection conn=null;
                try {
                    url = new URL("http://192.168.7.244:8080/webdata2/images/hgmm.jpeg");
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
                        Message msg=Message.obtain();
                        msg.obj=bitmap;
                        msg.what=PICOK;
                        handler.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(PICFAIL);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(PICFAIL);
                }finally {
                    try {
                        is.close();
                        conn.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();


    }
}
