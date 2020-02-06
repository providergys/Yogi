package com.yogi_app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.yogi_app.R;

import java.util.List;


/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class QrscanActivity extends Activity {
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null) {
                // Prevent duplicate scans
                return;
            }
            else {
                Intent webview = new Intent(QrscanActivity.this, WebViewActivity.class);
                webview.putExtra("web_url", String.valueOf(result.getText()));
                startActivity(webview);
            }
//                 System.out.println("fgggggggggggg"+result.getText());

//            Toast.makeText(QrscanActivity.this, "sdadff" + result.getText(), Toast.LENGTH_LONG).show();


        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scan_activity);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }




    @Override
    public void onBackPressed() {
        Intent newIntent = new Intent(QrscanActivity.this,MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        super.onBackPressed();
    }
}
