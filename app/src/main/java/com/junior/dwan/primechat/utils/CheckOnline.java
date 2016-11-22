package com.junior.dwan.primechat.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Might on 18.11.2016.
 */

public class CheckOnline {


   public static boolean isOnline(Context context) {

        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }
}
