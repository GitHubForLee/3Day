package com.robin.androidnet;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by robin on 2016/8/10.
 */
public class URLAct extends AppCompatActivity{
    private static final int PICOK =1 ;
    private static final int PICFAIL = 2;
    Button btnLoad;
    ImageView img;
    String imgUrl="http://192.168.7.10:8080/hello/images/hgmm.jpeg";
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PICOK:
                    img.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();


    }

    private void initView() {
        setContentView(R.layout.url_layout);
        btnLoad=(Button)findViewById(R.id.btnLoad);
        img=(ImageView)findViewById(R.id.imageView);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAsynTask();
            }
        });
    }

    private void startAsynTask() {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("连接网络");
        pd.setMessage("正在连接远程服务器,请稍后...");
        pd.show();

        new Thread(){
            @Override
            public void run() {

                try {
                    //1.创建一个url
                    URL url=new URL(imgUrl);
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
                       Bitmap bitmap= BitmapFactory.decodeStream(is);
                       Message msg=handler.obtainMessage();
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

                }
                pd.dismiss();
            }
        }.start();


    }
}
