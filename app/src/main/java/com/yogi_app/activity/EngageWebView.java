package com.yogi_app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.yogi_app.R;

public class EngageWebView extends AppCompatActivity {

    String web_url;
    WebView webview;
    String uRl="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uRl = extras.getString("URL");
        }
        //  Toast.makeText(EngageWebView.this,""+uRl,Toast.LENGTH_SHORT).show();
      //  Log.e("URL",""+uRl);
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(uRl);
        webview.setHorizontalScrollBarEnabled(false);
    }
}
