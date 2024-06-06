package com.example.beta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class InternetReceiver extends BroadcastReceiver {
    /**
     * makes a toast to inform the user when there isn't internet connection
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean status = CheckInternet.getNetworkInfo(context);
        if(status==false)
        {
            Toast.makeText(context, "please connect to network, without network connection the app will not work properly", Toast.LENGTH_LONG).show();
        }

    }
}
