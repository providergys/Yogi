package com.yogi_app.banbuser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.yogi_app.R;
import com.yogi_app.activity.MainActivity;
import com.yogi_app.fcm.MyFirebaseMessagingService;
import com.yogi_app.utility.TinyDB;

public class CameraBroadCastActivity extends AppCompatActivity {
    private static final String APPLICATION_ID = "IgDS5d3ym9DqexWtMRDMxQ";
    private SurfaceView mPreviewSurface;
    Broadcaster mBroadcaster;
    Button mBroadcastButton;
    ImageView backBtn;
    private TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        tinyDB = new TinyDB(this);
       // tinyDB.putString("video_id_s", MyFirebaseMessagingService.video_id_s);
        mPreviewSurface = findViewById(R.id.PreviewSurfaceView);
        backBtn = findViewById(R.id.back_btn);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());

        mBroadcastButton = (Button)findViewById(R.id.BroadcastButton);
        mBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBroadcaster.canStartBroadcasting())
                    mBroadcaster.startBroadcast();
                else
                    mBroadcaster.stopBroadcast();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBroadcaster.stopBroadcast();
                Intent intent = new Intent(CameraBroadCastActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        mBroadcaster.setCameraSurface(mPreviewSurface);
        mBroadcaster.onActivityResume();
        if (!hasPermission(Manifest.permission.CAMERA)
                && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBroadcaster.onActivityDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();
        mBroadcaster.onActivityPause();
    }
    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }



    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            Log.i("Mybroadcastingapp", "Received status change: " + broadcastStatus);
            if (broadcastStatus == BroadcastStatus.STARTING)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (broadcastStatus == BroadcastStatus.IDLE)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mBroadcastButton.setText(broadcastStatus == BroadcastStatus.IDLE ? "Broadcast" : "Disconnect");

        }

        @Override
        public void onStreamHealthUpdate(int i) {

        }

        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {
            Log.w("Mybroadcastingapp", "Received connection error: " + connectionError + ", " + s);
        }

        @Override
        public void onCameraError(CameraError cameraError) {

        }

        @Override
        public void onChatMessage(String s) {

        }

        @Override
        public void onResolutionsScanned() {

        }

        @Override
        public void onCameraPreviewStateChanged() {

        }

        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {

        }

        @Override
        public void onBroadcastIdAvailable(String s) {

        }
        // ...
    };
}
