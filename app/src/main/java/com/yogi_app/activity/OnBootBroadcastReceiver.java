package com.yogi_app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yogi_app.fcm.MyFirebaseMessagingService;

/**
 * Created by AndroidDev on 14-Nov-18.
 */

public class OnBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent("com.demo.FirebaseMessagingReceiveService");
        i.setClass(context, MyFirebaseMessagingService.class);
        context.startService(i);
    }
}