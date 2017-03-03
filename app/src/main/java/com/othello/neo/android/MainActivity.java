package com.othello.neo.android;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private serviceReceiver receiver;
    IntentFilter intentFilter;
    private TextView message_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiver = new serviceReceiver();
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

            Log.d("service", "Handler:" + message);

            Toast toast = Toast.makeText( MainActivity.this, "Hello", Toast.LENGTH_SHORT); toast.show();
            //message_tv.setText(message);

        }
    };
}

