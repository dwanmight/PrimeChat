package com.junior.dwan.primechat.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;

import com.junior.dwan.primechat.R;

/**
 * Created by Might on 18.11.2016.
 */

public class SnackBars {

    public static Snackbar showSnackBar(Context context,CoordinatorLayout layout, String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.color_snackbar_red));
        return snackbar;
    }
}
