package com.robin.mynet.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.robin.mynet.utils.HttpUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by robin on 2016/10/17.
 */
public class DownloadTask extends AsyncTask<String,Void,byte[]> {
    private Context context;
    private DownloadTask.CallbackListener callback;

    public DownloadTask(Context context,DownloadTask.CallbackListener callback){
        this.context=context;
        this.callback=callback;
    }

    @Override
    protected byte[] doInBackground(String... params) {
        return HttpUtils.loadDataForHttp(params[0]);
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

    public static interface CallbackListener{
        public void onResponse(String string);

    }

}
