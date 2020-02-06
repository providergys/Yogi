package com.yogi_app.fcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yogi_app.R;
import com.yogi_app.activity.Calender;
import com.yogi_app.activity.MainActivity;
import com.yogi_app.activity.Profile;
import com.yogi_app.banbuser.RecieverActivity;
import com.yogi_app.fragments.SongsFragment;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.TinyDB;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String title;
    public  static String video_id_s;
    String type_message="";
    //private TinyDB tinyDB = new TinyDB(MyFirebaseMessagingService.this);
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        //Displaying data in log
//        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "From: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "From: " + remoteMessage.getNotification().getTitle());
        title = remoteMessage.getNotification().getTitle();

        type_message = remoteMessage.getData().get("type");
        if(remoteMessage.getData().get("video_id")!=null){
            video_id_s = remoteMessage.getData().get("video_id");
        }

//        tinyDB.putString("video_id_str",remoteMessage.getData().get("video_id"));
      //  System.out.println("sedfioresdit...."+type_message+".."+tinyDB.getString("video_id_str"));
        System.out.println("sedfioresdit...."+type_message+".."+video_id_s);


        if (type_message.matches("livestream")) {
           // startActivity(new Intent(MyFirebaseMessagingService.this, RecieverActivity.class));
            MainActivity.onMessageStatusbarIndication(true );
        } else if (type_message.matches("event")) {


        } else if (type_message.matches("message")) {

        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload:" + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                Log.d("worksjbhdjh", "TRUE");
                try {
                    sendNotification(remoteMessage.getNotification().getBody());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                // Handle message within 10 seconds
                Log.d("worksjbhdjh", "FALSE");

            }
        }


//       if( isForeground("com.yogi_app")){
//           Log.d("worksjbhdjhfgkj","TRUE");
//
//       }else{
//           Log.d("worksjbhdjdflgklh","TRUE");
//
//       }
        String message = remoteMessage.getNotification().getBody();
        showNotification(message);
    }


    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {


        if (type_message.matches("livestream")) {
            // startActivity(new Intent(MyFirebaseMessagingService.this, RecieverActivity.class));
           // MainActivity.onMessageStatusbarIndication(true);

            try {
                Intent intent = new Intent(this, RecieverActivity.class);
                intent.putExtra("notif_live", "red_statusbar");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                    PendingIntent.FLAG_ONE_SHOT);
                PendingIntent contentIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(contentIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH).setBadgeIconType(0); // value = 1
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, notificationBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type_message.matches("event")) {
            try {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("notif_live", "red_statusbar");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                    PendingIntent.FLAG_ONE_SHOT);
                PendingIntent contentIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(contentIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH).setBadgeIconType(0); // value = 1
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, notificationBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type_message.matches("message")) {
            try {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("notif_live", "red_statusbar");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                    PendingIntent.FLAG_ONE_SHOT);
                PendingIntent contentIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(contentIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH).setBadgeIconType(0); // value = 1
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, notificationBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //    public boolean isForeground(String myPackage) {
//        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
//        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
//        return componentInfo.getPackageName().equals(myPackage);
//    }
    public void showNotification(String msg) {
        Log.d("mfsdrtdgfjhg", "" + msg);
    }

}

//    public void apiFetchSongs() {
//
//        MainApplication.getApiService().fetchSongs("text/plain").enqueue(new Callback<FaqResponse>() {
//            @Override
//            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
//                FaqResponse faqResponse = response.body();
//                if (response.body().getRespCode().matches("2013")) {
//                    //   Toast.makeText(getActivity(), "" + faqResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                    rvSongsAdapter = new SongsFragment.RvSongsAdapter(getActivity(), faqResponse.getSongsList());
//                    rvSongs.setAdapter(rvSongsAdapter);
//
//                    for (int i = 0; i <faqResponse.getSongsList().size() ; i++) {
//                        newList.add(faqResponse.getSongsList().get(i).getMediaID());
//
//                    }
//
//                } else {
//                    Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FaqResponse> call, Throwable t) {
//
//
//            }
//        });
//    }

