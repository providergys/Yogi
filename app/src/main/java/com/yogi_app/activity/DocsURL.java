package com.yogi_app.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.yogi_app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DocsURL extends AppCompatActivity {
    String docURL = "";
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docs_url);
        ButterKnife.bind(this);
        imageView = (ImageView)findViewById(R.id.imgBack);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            docURL = extras.getString("DOCURL");
        }

        webView.setWebViewClient(new AppWebViewClients());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + docURL);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Bind(R.id.webView)
    WebView webView;

    public class AppWebViewClients extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.loadUrl("javascript:(function() { " +
                    "document.querySelector('[role=\"toolbar\"]').remove();})()");


        }

    }

}
