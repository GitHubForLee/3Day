package com.robin.androidnet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

/**
 * Created by robin on 2016/8/9.
 */
public class ChatAct extends AppCompatActivity{
    private static final int CONOK = 1;
    private static final int CONFAIL =2 ;
    private static final int READOK =3 ;
    private static final int READFAIL =4 ;
    private static final int CHATEND = 5;
    private static final String TAG = "robin debug";
    EditText editContent;
    Button btnSend;
    ListView listChat;
    ArrayList<String> arr;
    ArrayAdapter<String> aa;

    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    String content;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CONOK:
                    showToast("connect server success");
                    break;
                case CONFAIL:
                    showToast("connect server fail");
                    break;
                case READOK:
                    arr.add(content);
                    refreshListView();
                    break;

                case READFAIL:
                    //showToast("read server fail");
                    Log.e(TAG,"read server fail");
                    break;
                case CHATEND:
                    showToast("chat end");
                    btnSend.setEnabled(false);
                    btnSend.setClickable(false);
                    editContent.setEnabled(false);
                    break;
            }
        }
    };

    private void refreshListView() {
        aa.notifyDataSetChanged();

    }

    void showToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        connectServer();


    }

    private void connectServer() {
        new Thread(){
            @Override
            public void run() {
                socket=new Socket();
                SocketAddress remote=new InetSocketAddress("192.168.7.102",911);
                try {
                    socket.connect(remote,5000);
                    dis=new DataInputStream(socket.getInputStream());
                    dos=new DataOutputStream(socket.getOutputStream());
                    handler.sendEmptyMessage(CONOK);
                    readThread();//连接成功,启动读线程
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(CONFAIL);
                }
            }
        }.start();



    }

    private void readThread() {
        new Thread(){
            @Override
            public void run() {
                while(true){
                    try {
                        String s=dis.readUTF();
                        content="对方说:"+s;//读server数据
                        handler.sendEmptyMessage(READOK);
                        if("quit".equals(s)){
                            handler.sendEmptyMessage(CHATEND);
                            Log.e(TAG,"==========quit===========");
                            break;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(READFAIL);
                    }

                }

            }
        }.start();




    }

    private void initView() {
        setContentView(R.layout.chat_layout);
        editContent=(EditText)findViewById(R.id.editContent);
        btnSend=(Button)findViewById(R.id.btnSend);
        listChat=(ListView)findViewById(R.id.listChat);
        arr=new ArrayList<>();
        aa=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr);
        listChat.setAdapter(aa);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=editContent.getText().toString();
                try {
                    dos.writeUTF(s);
                    arr.add("自己说:" + s);
                    refreshListView();
                    editContent.setText("");
                    editContent.requestFocus();
                    if("quit".equals(s)){
                        showToast("chat end");
                        btnSend.setEnabled(false);
                        editContent.setEnabled(false);
                        btnSend.setClickable(false);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
