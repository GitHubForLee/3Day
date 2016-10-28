package com.robin.androidnet.asysntask;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by robin on 2016/8/10.
 */
public class CopyApkTask extends AsyncTask<InputStream,Void,Boolean> {

    private static final String TAG ="robin debug" ;
    private Context context;
    public CopyApkTask(Context context){
        this.context=context;
    }

    @Override
    protected Boolean doInBackground(InputStream... params) {

        FileOutputStream fos=null;
        InputStream inputStream=params[0];
        boolean result=false;
        try {
            //Log.e(TAG,"is available:"+inputStream.available());
            //存放下载apk的文件路径
            File file=new File(Environment.getExternalStorageDirectory(),"myapp.apk");
            fos=new FileOutputStream(file);
            byte[] buf=new byte[1024];
            int num=-1;
            while ((num=inputStream.read(buf))!=-1){
                fos.write(buf,0,num);
                fos.flush();
            }
            result =true;
        } catch (IOException e) {
            e.printStackTrace();
            result=false;
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }


    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if(result){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), "myapp.apk")),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
            Log.e(TAG,"OKOKOK");
        }






    }
}
