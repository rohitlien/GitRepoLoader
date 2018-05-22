package com.rohit.gitrepoloader.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by oust on 5/20/18.
 */

public class GitLoaderTools {
    public static boolean checkInternetStatus() {
        ConnectivityManager cm = (ConnectivityManager) AppController.getmContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }

    public static void showToast(String message){
        Toast.makeText(AppController.getmContext(), ""+message, Toast.LENGTH_SHORT).show();
    }
}
