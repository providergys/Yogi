package com.yogi_app.utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by andriod-dev on 3/30/2017.
 */

public class IsInternetOnGlobal {

    public static boolean isInternetOn(Activity ac)
    {

        ConnectivityManager connec = (ConnectivityManager) ac.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)
                || (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING)
                || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING)
                || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        }

        else if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED)
                || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED)) {
            return false;
        }

        return false;
    }

}
