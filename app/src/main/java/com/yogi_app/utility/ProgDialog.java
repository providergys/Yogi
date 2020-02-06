package com.yogi_app.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import com.yogi_app.R;


/**
 * Created by AndroidDev on 4/19/2017.
 */

public class ProgDialog {

    ProgressDialog progress;

    public void progDialog(Activity ac) {

        progress = new ProgressDialog( ac );
        progress.setMessage( "Please wait..." );
        progress.setInverseBackgroundForced( true );
        progress.getWindow().setBackgroundDrawable( new ColorDrawable( ContextCompat.getColor( ac, R.color.colorPrimary)));
        progress.setProgressStyle( ProgressDialog.STYLE_SPINNER );
        progress.setIndeterminate( true );
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progress.dismiss();
            }
        }, 3000);

            progress.show();

    }

    public void hideProg() {
        progress.dismiss();
    }
}
