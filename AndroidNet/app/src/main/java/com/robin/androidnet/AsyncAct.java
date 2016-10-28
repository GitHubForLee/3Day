package com.robin.androidnet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.robin.androidnet.asysntask.LoadImgTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by robin on 2016/8/10.
 */
public class AsyncAct extends AppCompatActivity {
    private static final int PICOK =1 ;
    private static final int PICFAIL = 2;
    private static final String TAG = "robin debug";
    Button btnLoad;
    ImageView img;
    String imgUrl="http://192.168.7.10:8080/hello/images/hgmm.jpeg";

    class LoadImageTask extends AsyncTask<String,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                //1.创建一个url
                URL url=new URL(params[0]);//url
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

                    return bitmap;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                img.setImageBitmap(bitmap);
            }


        }
    }


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
//                startAsynTask1();
                startAsynTask2();

            }
        });
    }

    private void startAsynTask2() {
        new LoadImgTask(this, new LoadImgTask.CallBackListener() {
            @Override
            public void onRespone(Bitmap bitmap) {
                img.setImageBitmap(bitmap);
            }
        }).execute(imgUrl);

    }

    private void startAsynTask1() {
        new LoadImageTask().execute(imgUrl);
    }
}
