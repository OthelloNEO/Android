package com.othello.neo.android;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ServiceReceiver receiver;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver = new ServiceReceiver();
        receiver.registerHandler(updateHandler);
        intentFilter = new IntentFilter();
        intentFilter.addAction("UPDATE_ACTION");
        registerReceiver(receiver, intentFilter);
        Intent intent = new Intent(MainActivity.this,LoginService.class);
        this.startService(intent);
    }
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            String message = bundle.getString("message");
            Log.d("service", "Receiver:" + message);

            if(message.matches("loginok")){
                Log.d("service", "loginok");
                Intent intent=new Intent(MainActivity.this,SelectMenuActivity.class);
                startActivity(intent);
            }
            Toast toast = Toast.makeText( MainActivity.this, message, Toast.LENGTH_SHORT); toast.show();
        }
    };
}

