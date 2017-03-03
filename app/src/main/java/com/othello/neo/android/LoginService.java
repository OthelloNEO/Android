package com.othello.neo.android;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static android.R.id.message;

/**
 * Created by Tomoyuki on 2017/03/01.
 */

public class LoginService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LoginService(String name) {
        super(name);
    }
    public LoginService(){
        super("LoginService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("IntentService", "onHandleIntent Start");
/*        long endTime = System.currentTimeMillis() + 5*1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        }*/
        /*Runnable sender = new Runnable(){
             @Override
             public void run() {
                 Socket socket;
                 String address = "192.168.56.1";
                 String strPort = "8080";
                 int port = Integer.parseInt(strPort);
                 String username_str = "test";
                 String password_str = "test";
                 String line = null;
                 try {
                     socket = new Socket(address,port);
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                     String sendTxt = "login-"+username_str+"-"+password_str;
                     writer.write(sendTxt);
                     writer.close();
                     InputStream input = socket.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                     line = reader.readLine();
                     sendBroadCast("line:"+line);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }

             }
        };
        Thread th = new Thread(sender);
        th.start();*/
        sendBroadCast("onHandleIntent Finish");
    }

    protected void sendBroadCast(String message) {

        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra("message", message);
        broadcastIntent.setAction("UPDATE_ACTION");
        getBaseContext().sendBroadcast(broadcastIntent);

    }

}
