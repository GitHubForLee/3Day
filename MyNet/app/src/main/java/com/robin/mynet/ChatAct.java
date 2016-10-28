package com.robin.mynet;

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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

/**
 * Created by robin on 2016/10/14.
 */
public class  ChatAct extends AppCompatActivity {
    private static final int CONNECTOK = 1;
    private static final int CONNECTFAIL = 2;
    private static final int READOK = 3;
    private static final int RWFAIL = 4;
    private static final int CHATEND =5 ;
    private static final String TAG ="robin debug" ;
    ListView listView;
    ArrayList<String> arr;
    ArrayAdapter<String> adapter;
    EditText editContent;
    Button btnSend;
    Socket socket=null;
    InputStream is=null;
    OutputStream os=null;
    DataInputStream dis=null;
    DataOutputStream dos=null;
    String content;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CONNECTOK:
                    showToast("server connect success");
                    break;
                case CONNECTFAIL:
                    showToast("server connect fail");
                    break;
                case READOK:
                    Log.e(TAG,content);
                    arr.add(content);
                    adapter.notifyDataSetChanged();
                    break;
                case RWFAIL:
                    showToast("write|read server faile");
                    break;
                case CHATEND:
                    showToast("chat end");
                    btnSend.setEnabled(false);
                    editContent.setEnabled(false);
                    btnSend.setClickable(false);
                    break;
            }


        }
    };

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_act);
        initView();

        //1.启动activity自动连接tcp server
        connectServer();


    }

    private void createReadThread() {
        new Thread(){
            @Override
            public void run() {
                while(true){
                    try {
                        String s=dis.readUTF();
                        content="对方说:"+s;//读server的数据
                        Log.e(TAG,">>>"+s);
                        handler.sendEmptyMessage(READOK);
                        if("quit".equals(s)){
                            handler.sendEmptyMessage(CHATEND);
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(RWFAIL);
                    }
                }
            }
        }.start();
    }

    private void connectServer() {
        new Thread(){
            @Override
            public void run() {
                try {
                    //socket=new Socket("192.168.7.243",911);//网络操作会阻塞
                    socket=new Socket();
                    SocketAddress remote=new InetSocketAddress("192.168.7.243",911);
                    socket.connect(remote,5000);
                    is=socket.getInputStream();
                    os=socket.getOutputStream();
                    dis=new DataInputStream(is);
                    dos=new DataOutputStream(os);
                    handler.sendEmptyMessage(CONNECTOK);

                    //2.启动读线程
                    createReadThread();
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(CONNECTFAIL);
                }
            }
        }.start();


    }

    private void initView() {
        listView=(ListView)findViewById(R.id.listView);
        arr=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr);
        listView.setAdapter(adapter);

        editContent=(EditText)findViewById(R.id.editContent);
        btnSend=(Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=editContent.getText().toString();
                editContent.setText("");
                try {
                    dos.writeUTF(s);
                    arr.add("自己说:" + s);
                    adapter.notifyDataSetChanged();
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
