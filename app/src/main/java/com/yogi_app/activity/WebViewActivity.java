package com.yogi_app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yogi_app.R;

public class WebViewActivity extends Activity {

    String web_url;
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        web_url = getIntent().getStringExtra("web_url");
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(web_url);
        webview.setHorizontalScrollBarEnabled(false);
    }


}
