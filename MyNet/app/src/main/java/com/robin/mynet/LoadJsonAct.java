package com.robin.mynet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.robin.mynet.asynctask.DownloadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by robin on 2016/10/17.
 */
public class LoadJsonAct extends AppCompatActivity {
    Button btn;
    TextView txtResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_act);
        initView();
    }

    private void initView() {
        btn=(Button)findViewById(R.id.btnLaodjson);
        txtResult=(TextView)findViewById(R.id.txtResult);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAsyncTask();
            }
        });
    }

    private void startAsyncTask() {
        new DownloadTask(this, new DownloadTask.CallbackListener() {
            @Override
            public void onResponse(String string) {

                try {
                    JSONObject jsonObject=new JSONObject(string);
                    JSONArray array=jsonObject.getJSONArray("carte");
                    StringBuilder builder=new StringBuilder();
                    for(int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        String str=""+obj.getInt("id");
                        str=str+","+obj.getInt("prices");
                        str=str+","+obj.getString("name");
                        builder.append(str+"\n");
                    }
                    txtResult.setText(builder.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("http://192.168.7.244:8080/webdata2/GetJsonData?req=one");


    }
}
