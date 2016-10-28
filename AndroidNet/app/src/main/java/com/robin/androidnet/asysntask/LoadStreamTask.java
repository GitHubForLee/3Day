package com.robin.androidnet.asysntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.robin.androidnet.httputils.HttpUitls;

import java.io.InputStream;

/**
 * Created by robin on 2016/8/10.
 */
public class LoadStreamTask extends AsyncTask<String,Void,InputStream> {
    private Context context;
    private ProgressDialog pd=null;
    private CallBackListener callback;

    public LoadStreamTask(Context context,CallBackListener callback){
        this.context=context;
        this.callback=callback;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(context);
        pd.setTitle("连接网络");
        pd.setMessage("正在连接远程服务器,请稍后...");
        pd.show();
    }

    @Override
    protected InputStream doInBackground(String... params) {
        return HttpUitls.LoadStreamForHttp(params[0]);
    }

    @Override
    protected void onPostExecute(InputStream inputStream) {
        super.onPostExecute(inputStream);
        if(inputStream!=null&&callback!=null){
            callback.onResponse(inputStream);
        }
        pd.dismiss();
    }

    public  static interface CallBackListener{
        public void onResponse(InputStream inputStream);

    }


}
