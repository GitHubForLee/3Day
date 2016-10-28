package com.robin.androidnet.asysntask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.robin.androidnet.httputils.HttpUitls;

/**
 * Created by robin on 2016/8/10.
 */

//针对图片加载 返回 btimap对象
public class LoadImgTask extends AsyncTask<String,Void,byte[]>{
    private String imgUrl;
    private Context context;
    private CallBackListener callback;
    public LoadImgTask(Context context,CallBackListener callback){
        this.context=context;
        this.callback=callback;
    }

    @Override
    protected byte[] doInBackground(String... params) {
        imgUrl=params[0];
        return HttpUitls.loadDataForHttp(imgUrl);
    }

    @Override
    protected void onPostExecute(byte[] result) {
        super.onPostExecute(result);
        if(result!=null&&callback!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(result,0,result.length);
            callback.onRespone(bitmap);
        }

    }

    public static  interface CallBackListener{

        public void onRespone(Bitmap bitmap);

    }

}
