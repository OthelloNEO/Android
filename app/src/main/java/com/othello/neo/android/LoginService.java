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
         try {
             Log.d("IntentService", "onHandleIntent Start");
             Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        onDestroy();
    }


}
