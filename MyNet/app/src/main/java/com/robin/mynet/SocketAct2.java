package com.robin.mynet;

import android.os.AsyncTask;
import android.os.Bundle;
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
public class SocketAct2 extends AppCompatActivity {
    private static final int CONNECTOK = 1;
    private static final int CONNECTFAIL = 2;
    private static final int READOK = 3;
    private static final int RWFAIL = 4;
    private static final int CONNECTSRV = 5;
    private static final int RWSRV = 6;
    EditText edit;
    TextView txtResult;
    Button btnLogin;
    Socket socket = null;
    InputStream is = null;
    OutputStream os = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    String instr1;

    class MyTask extends AsyncTask<Integer, Object, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int op = params[0];
            int result = -1;
            switch (op) {
                case CONNECTSRV:
                    result = connectServer();
                    break;
                case RWSRV:
                    result = loginServer();
                    break;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer op) {
            super.onPostExecute(op);
            if (op != null) {
                switch (op) {
                    case CONNECTOK:
                        showToast("server connect success");
                        break;
                    case CONNECTFAIL:
                        showToast("server connect fail");
                        break;
                    case READOK:
                        txtResult.setText(instr1);
                        break;
                    case RWFAIL:
                        showToast("write|read server faile");
                }
            }
        }
    }


    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_act);
        initView();
        new MyTask().execute(CONNECTSRV);
    }

    private int connectServer() {
        int result = -1;
        try {
            socket = new Socket("192.168.7.243", 2345);//网络操作会阻塞
            is = socket.getInputStream();
            os = socket.getOutputStream();
            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);
            result = CONNECTOK;
        } catch (IOException e) {
            e.printStackTrace();
            result = CONNECTFAIL;
        }
        return result;
    }


    private void initView() {
        edit = (EditText) findViewById(R.id.editPwd);
        txtResult = (TextView) findViewById(R.id.txtResult);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute(RWSRV);
                btnLogin.setEnabled(false);
            }
        });
    }

    private int loginServer() {
        try {
            dos.writeUTF(edit.getText().toString());
            instr1 = dis.readUTF();
            return READOK;

        } catch (IOException e) {
            e.printStackTrace();
            return RWFAIL;
        } finally {
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
}
