package com.robin.androidnet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.robin.androidnet.asysntask.CopyApkTask;
import com.robin.androidnet.asysntask.DownLoadTask;
import com.robin.androidnet.asysntask.LoadStreamTask;
import com.robin.androidnet.utils.UpgradeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

//兼容低版本activity
public class MainActivity extends AppCompatActivity {

    private static final String TAG ="robin debug" ;
    ListView list;
    String[] functions={"TCPsocket基本通信","TCPsocket聊天","handler访问web资源",
            "asynctask访问web资源","访问json","检查更新"

    };
    TextView txtTile;
    int verCode;
    String verName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTile=(TextView)findViewById(R.id.txtTitle);
        verCode=UpgradeUtils.getVerCode(this);
        verName=UpgradeUtils.getVerName(this);
        txtTile.append(" 版本名:" + UpgradeUtils.getVerName(this)+" ,版本号:"+UpgradeUtils.getVerCode(this));


        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, functions));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        doTcpSocket();
                        break;
                    case 1:
                        doChat();
                        break;
                    case 2:
                        doURL();
                        break;
                    case 3:
                        doAsynTask();
                        break;
                    case 4:
                        doJson();
                        break;
                    case 5:
                        doUpgrade();
                        break;
                }
            }
        });

    }

    private void doUpgrade() {
        Log.e(TAG, "upgrade...");
        checkVersion();
    }

    private void checkVersion() {
        String verUrl="http://192.168.7.244:8080/webdata2/GetVer";
        //启动访问verUrl的异步任务
        new DownLoadTask(this, new DownLoadTask.CallBackListener() {
            @Override //异步任务结束时才能调用
            public void onResponse(String string) {
                try {

                    JSONArray array=new JSONArray(string);
                    if(array.length()>0){
                        JSONObject obj=array.getJSONObject(0);
                        int newVerCode=obj.getInt("verCode");
                        String newVerName=obj.getString("verName");
                        if(newVerCode>verCode){
                            String msg="当前版本:"+verName+" code:"+verCode+",发现新版本:"
                                    +newVerName+" code:"+newVerCode+",是否更新?";
                            Dialog dialog=new AlertDialog.Builder(MainActivity.this).setTitle("软件更新")
                                    .setMessage(msg)
                                    .setPositiveButton("更新",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startLoadApk();//异步获取网络输入流
                                                    dialog.dismiss();
                                                }
                                            }

                                    ).setNegativeButton("暂不更新",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).create();
                            dialog.show();
                        }else{
                            String msg="当前版本:"+verName+" code:"+verCode+"已是最新版本,无需更新!";
                            Dialog dialog=new AlertDialog.Builder(MainActivity.this).setTitle("软件更新")
                                    .setMessage(msg)
                                    .setPositiveButton("确定",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }

                                    ).create();
                            dialog.show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(verUrl);






    }

    private void startLoadApk() {
        Log.e(TAG,"下载apk....");
        String apkUrl="http://192.168.7.244:8080/webdata2/apk/app2.apk";
        new LoadStreamTask(this, new LoadStreamTask.CallBackListener() {
            @Override
            public void onResponse(InputStream inputStream) {

                startCopyApk(inputStream);


            }
        }).execute(apkUrl);







    }

    private void startCopyApk(InputStream inputStream) {

        new CopyApkTask(this).execute(inputStream);



    }

    private void doJson() {
        startActivity(new Intent(this,JSONAct.class));
    }

    private void doAsynTask() {
        startActivity(new Intent(this,AsyncAct.class));
    }

    private void doURL() {
        startActivity(new Intent(this,URLAct.class));
    }

    private void doChat() {
        startActivity(new Intent(this,ChatAct.class));
    }

    private void doTcpSocket() {
        startActivity(new Intent(this,TcpSocketAct.class));

    }



}
