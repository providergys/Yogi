package com.yogi_app.utility;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.yogi_app.R;


/**
 * Created by AndroidDev on 4/19/2017.
 */

public class SnakeBaar {

    public  void showSnackBar(Activity ac, String message, View lay) {
        Snackbar snackbar = Snackbar
                .make(lay, message, Snackbar.LENGTH_LONG)
                .setAction("OK", onSnackBarClickListener());

        snackbar.setActionTextColor(ac.getResources().getColor(R.color.colorAccent));
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.WHITE);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        //  textView.setTextColor(getResources().getColor(R.color.buttonbackground));
        textView.setTextColor(ContextCompat.getColor(ac, R.color.colorAccent));
        snackbar.show();

    }

    private View.OnClickListener onSnackBarClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(LoginActivity.this, "You clicked SnackBar Button", Toast.LENGTH_SHORT).show();

            }
        };
    }
}
