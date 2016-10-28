package com.robin.mynet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by robin on 2016/10/14.
 */
public class SocketAct extends AppCompatActivity {
    private static final int CONNECTOK = 1;
    private static final int CONNECTFAIL = 2;
    private static final int READOK = 3;
    private static final int RWFAIL = 4;
    EditText edit;
    TextView txtResult;
    Button btnLogin;
    Socket socket=null;
    InputStream is=null;
    OutputStream os=null;
    DataInputStream dis=null;
    DataOutputStream dos=null;


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
                    txtResult.setText(msg.obj.toString());
                    break;
                case RWFAIL:
                    showToast("write|read server faile");
            }


        }
    };

    private void showToast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_act);
        initView();
        connectServer();
    }

    private void connectServer() {

            new Thread(){
                @Override
                public void run() {
                    try {
                        socket=new Socket("192.168.7.243",2345);//网络操作会阻塞
                        is=socket.getInputStream();
                        os=socket.getOutputStream();
                        dis=new DataInputStream(is);
                        dos=new DataOutputStream(os);
                        handler.sendEmptyMessage(CONNECTOK);
                    } catch (IOException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(CONNECTFAIL);
                    }
                }
            }.start();
    }

    private void initView() {
        edit=(EditText)findViewById(R.id.editPwd);
        txtResult=(TextView)findViewById(R.id.txtResult);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginServer();
                btnLogin.setEnabled(false);
            }
        });
    }

    private void loginServer() {

        new Thread(){
            @Override
            public void run() {
                try {
                    dos.writeUTF(edit.getText().toString());
                    String instr1=dis.readUTF();
                    Message msg=Message.obtain();
                    msg.obj=instr1;
                    msg.what=READOK;
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(RWFAIL);
                }finally {
                    try {
                        dis.close();
                        dos.close();
                        os.close();
                        is.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();



    }
}
