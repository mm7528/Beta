package com.example.beta;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternet {

    /**
     * Gets network info.
     *
     * @param context the context
     * @return the network info
     */
    public static boolean getNetworkInfo(Context context)
    {
        boolean status = false;
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null)
        {
            status=true;
        }
        else {
            status=false;
        }
        return status;
    }
}
