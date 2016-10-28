package com.robin.mynet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doButton(View view){
        switch (view.getId()){
            case R.id.btnSocket:
                doSocket();
                break;
            case R.id.btnsocket2:
                doSocket2();


            case R.id.btnChat:
                doChat();
                break;

            case R.id.btnUrl1:
                doURLHandler();
                break;
            case R.id.btnUrl2:
                doURLAsync();
                break;
            case R.id.btnJson:
                doJSON();
                break;

        }

    }

    private void doJSON() {
        startActivity(new Intent(this,LoadJsonAct.class));
    }

    private void doURLAsync() {
        startActivity(new Intent(this,AsyncTaskAct.class));
    }

    private void doURLHandler() {
        startActivity(new Intent(this,UrlHandlerAct.class));
    }

    private void doChat() {
        startActivity(new Intent(this,ChatAct.class));
    }

    private void doSocket2() {
        startActivity(new Intent(this,SocketAct2.class));
    }

    private void doSocket() {
        startActivity(new Intent(this,SocketAct.class));
    }
}
