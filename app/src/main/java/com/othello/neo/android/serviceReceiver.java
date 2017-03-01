package com.othello.neo.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Tomoyuki on 2017/03/01.
 */

public class serviceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String message = bundle.getString("message");
        Log.d("serviceReceiver", "Receive message");
        Toast.makeText(
                context,
                "onReceive! " + message,
                Toast.LENGTH_LONG).show();
    }
}
