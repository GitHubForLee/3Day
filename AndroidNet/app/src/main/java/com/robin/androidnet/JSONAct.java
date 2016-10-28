package com.robin.androidnet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.robin.androidnet.asysntask.DownLoadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by robin on 2016/8/10.
 */
public class JSONAct extends AppCompatActivity {
    Button btnJson;
    TextView txtJson;
    String jsonUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        jsonUrl="http://192.168.7.10:8080/webdata2/GetJsonData?req=one";

    }

    private void initView() {
        setContentView(R.layout.json_layout);
        btnJson=(Button)findViewById(R.id.btnLoadJson);
        txtJson=(TextView)findViewById(R.id.txtJson);
        btnJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoad();
            }
        });

    }

    private void startLoad() {

        new DownLoadTask(this, new DownLoadTask.CallBackListener() {
            @Override
            public void onResponse(String string) {
                try {
                    JSONObject jsonObject=new JSONObject(string);
                    JSONArray array=jsonObject.getJSONArray("carte");
                    StringBuilder builder=new StringBuilder();
                    for(int i=0;i<array.length();i++){
                        JSONObject obj= array.getJSONObject(i);
                        String str=""+obj.getInt("id");
                        str=str+","+obj.getInt("prices");
                        str=str+","+obj.getString("name");
                        builder.append(str+"\n");
                    }
                    txtJson.setText(builder.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(jsonUrl);

    }


}
