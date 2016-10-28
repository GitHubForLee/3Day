package com.robin.androidnet.asysntask;

import android.content.Context;
import android.os.AsyncTask;

import com.robin.androidnet.httputils.HttpUitls;

import java.io.UnsupportedEncodingException;

/**
 * Created by robin on 2016/8/10.
 */

//针对字符串文本资源
public class DownLoadTask extends AsyncTask<String,Void,byte[]> {
    private Context context;
    private CallBackListener callback;

    public DownLoadTask(Context context,CallBackListener callback){
        this.context=context;
        this.callback=callback;

    }

    @Override
    protected byte[] doInBackground(String... params) {

        return HttpUitls.loadDataForHttp(params[0]);
    }

    @Override
    protected void onPostExecute(byte[] result) {
        super.onPostExecute(result);

        if(result!=null&&callback!=null){
            try {
                callback.onResponse(new String(result,"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


    }

    public static interface CallBackListener{
        public void onResponse(String string);
    }

}
