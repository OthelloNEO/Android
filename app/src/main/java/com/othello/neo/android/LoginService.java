package com.othello.neo.android;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.Socket;

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
        long endTime = System.currentTimeMillis() + 5*1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        }
             /*       Runnable semder = new Runnable(){
                        @Override
                        public void run() {
                            Socket socket = null;
                            String address = "localhost";
                            String strPort = "8080";
                        }
                    };
               */

            String message = "メッセージ";
            sendBroadCast(message);



    }

    protected void sendBroadCast(String message) {

        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra("message", message);
        broadcastIntent.setAction("UPDATE_ACTION");
        getBaseContext().sendBroadcast(broadcastIntent);

    }

}
