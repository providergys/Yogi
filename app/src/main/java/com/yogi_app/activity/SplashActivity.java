package com.yogi_app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.yogi_app.R;
import com.yogi_app.utility.TinyDB;

import java.util.Locale;


public class SplashActivity extends Activity {


    private static int counter;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    Activity ac;
    protected boolean _active = true;
    TinyDB tiny_db;
    protected int _splashTime = 1;/*500*/
    String langval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Locale.getDefault().getLanguage();
        System.out.println("fgggggggggggggggggggggh"+ Locale.getDefault().getLanguage());
        if(Locale.getDefault().getLanguage().matches("en")){

            langval="en";
            Configuration config = getBaseContext().getResources().getConfiguration();
            Locale locale = new Locale(langval);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        }else {

            langval="zh";
            Configuration config = getBaseContext().getResources().getConfiguration();
            Locale locale = new Locale(langval);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        }

        tiny_db=new TinyDB(SplashActivity.this);

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {

                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(200);

                        if (_active) {
                            waited += 200;
                        }
                    }
                } catch (Exception e) {

                } finally {
                }
            }

            ;
        };
        splashTread.start();

        progMethod();
    }

    public void progMethod() {

        counter = 0;

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setMax(200);
        new Thread(new Runnable() {

            public void run() {
                while (progressStatus < 200) {
                    progressStatus = doSomeWork();

                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                           /* new DotProgressBarBuilder(ac)
                                    .setDotAmount(5)
                                    .setStartColor(Color.BLACK)
                                    .setAnimationDirection(DotProgressBar.LEFT_DIRECTION)
                                    .build();*/

                        }
                    });

                }

                handler.post(new Runnable() {
                    public void run() {
                        if(tiny_db.getString("id").isEmpty()){
                            Intent mainIntent = new Intent(SplashActivity.this, Login.class);
                            startActivity(mainIntent);
                            finish();
                        }else{
                            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }
                        //  progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
            //---do some long lasting work here---
            private int doSomeWork() {
                try {
                    //---simulate doing some work---
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ++counter;
            }
        }).start();

    }
}
