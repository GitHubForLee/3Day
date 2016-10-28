package com.robin.androidnet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by robin on 2016/8/9.
 */
public class TcpSocketAct extends AppCompatActivity{
    private static final String TAG = "robin debug";
    private static final int CONNECTOK =1 ;
    private static final int CONNECTFAIL = 2;
    private static final int READOK =3 ;
    private static final int READFAIL = 4;
    EditText editText;
    TextView textView;
    Button button;
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CONNECTOK:
                    showToast("server connect success");
                    break;
                case CONNECTFAIL:
                    showToast("server connect fail.");
                    break;
                case READOK:
                    String response=msg.obj.toString();
                    textView.setText(response);
                    button.setEnabled(false);
                    break;
                case READFAIL:
                    showToast("read fail");
                    break;
            }


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();

        connect();
        Log.e(TAG, "onCreate over");

    }

    private void connect() {
        final ProgressDialog pd=new ProgressDialog(TcpSocketAct.this);
        pd.setTitle("连接网络");
        pd.setMessage("正在连接远程服务器,请稍后...");
        pd.show();

        new Thread(){
            @Override
            public void run() {
                try {
//            socket=new Socket("192.168.7.100",2345);//产生网络阻塞
                    socket=new Socket();
                    SocketAddress remote=new InetSocketAddress("192.168.7.100",2345);
                    socket.connect(remote,5000);//产生网络阻塞
                    dis=new DataInputStream(socket.getInputStream());
                    dos=new DataOutputStream(socket.getOutputStream());
                    handler.sendEmptyMessage(CONNECTOK);

                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(CONNECTFAIL);
                }
                pd.dismiss();
            }
        }.start();
    }

    private void initView() {
        editText=(EditText)findViewById(R.id.editText1);
        textView=(TextView)findViewById(R.id.textView1);
        button=(Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginServer();
            }
        });
    }

    private void loginServer() {
        new Thread(){
            @Override
            public void run() {
                try {
                    dos.writeUTF(editText.getText().toString());
                    String response = dis.readUTF();

                    Message msg=handler.obtainMessage();
                    msg.what=READOK;
                    msg.obj=response;
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(READFAIL);
                } finally {
                    try {
                        dis.close();
                        dos.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    void showToast(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    class MyAsynTask extends AsyncTask<Integer,Void,Integer>{


        @Override
        protected Integer doInBackground(Integer... params) {
            switch (params[0]){

                case 0://connect server
                    try {
//            socket=new Socket("192.168.7.100",2345);//产生网络阻塞
                        socket=new Socket();
                        SocketAddress remote=new InetSocketAddress("192.168.7.100",2345);
                        socket.connect(remote,5000);//产生网络阻塞
                        dis=new DataInputStream(socket.getInputStream());
                        dos=new DataOutputStream(socket.getOutputStream());
                       return CONNECTOK;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return CONNECTFAIL;
                    }


                case 1://login server
                    try {
                        //dos.writeUTF(editText.getText().toString());
                       // String response = dis.readUTF();
//                        dos.writeUTF(params[1]);
                        dos.writeUTF("pwd");
                       return READOK;

                    } catch (IOException e) {
                        e.printStackTrace();
                        return READFAIL;
                    } finally {
                        try {
                            dis.close();
                            dos.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

            }
            return -1;
        }




    }

    void foo(){
        //connect server
        new MyAsynTask().execute(0);
//        new MyAsynTask().execute("0");
        //read server
        new MyAsynTask().execute(1);
//        new MyAsynTask().execute("1",editText.getText().toString());

    }


}
