package com.yogi_app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
//import com.tencent.mm.sdk.modelpay.PayReq;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yogi_app.R;
import com.yogi_app.alipay.H5PayDemoActivity;
import com.yogi_app.alipay.PayDemoActivity;
import com.zapp.library.merchant.util.PBBAAppUtils;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.transform.Result;

import de.wirecard.paymentsdk.WirecardClient;
import de.wirecard.paymentsdk.WirecardClientBuilder;
import de.wirecard.paymentsdk.WirecardEnvironment;
import de.wirecard.paymentsdk.WirecardException;
import de.wirecard.paymentsdk.WirecardPaymentResponse;
import de.wirecard.paymentsdk.WirecardPaymentType;
import de.wirecard.paymentsdk.WirecardResponseError;
import de.wirecard.paymentsdk.WirecardResponseListener;
import de.wirecard.paymentsdk.WirecardTransactionType;
import de.wirecard.paymentsdk.models.WirecardCardPayment;
import de.wirecard.paymentsdk.models.WirecardPBBAPayment;
import de.wirecard.paymentsdk.models.WirecardPayPalPayment;
import de.wirecard.paymentsdk.models.WirecardPayment;
import de.wirecard.paymentsdk.models.WirecardSepaPayment;
//import io.github.mayubao.pay_library.AliPayReq2;
//import io.github.mayubao.pay_library.PayAPI;
//import io.github.mayubao.pay_library.WechatPayReq;

public class PayPalPaymentGateway extends AppCompatActivity implements View.OnClickListener {
    private static final String ENCRYPTION_ALGORITHM = "HS256";
    private static final String UTF_8 = "UTF-8";
    private WirecardClient wirecardClient;
    private TextView resultLabel, ali_pay;
    PayPalPaymentGateway activity;
    GPSTracker gpsTrack;
    public static double latitude = 0;
    public static double longitude = 0;
    String countryCode, code;
    //    IWXAPI api;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("APP-80W284485P519543T");
    Handler handler1;
    Thread thread1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypal_payment);
        gpsTrack = new GPSTracker(PayPalPaymentGateway.this);
        try {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            code = tm.getNetworkCountryIso();
            Log.d("Countrycodiewwe", "" + code);
        } catch (Exception e) {
            e.printStackTrace();

        }

        String environment = WirecardEnvironment.TEST.getValue();
        try {
            wirecardClient = WirecardClientBuilder.newInstance(this, environment)
                    .setRequestTimeout(60)
                    .build();
        } catch (WirecardException exception) {
            Log.d(de.wirecard.paymentsdk.BuildConfig.APPLICATION_ID, "device is rooted");
        }

        //resultLabel = (TextView) findViewById(R.id.result);
        setOnClickListeners();
        if (gpsTrack.canGetLocation()) {
            latitude = gpsTrack.getLatitude();
            longitude = gpsTrack.getLongitude();

            Log.e("GPSLat", "" + latitude);
            Log.e("GPSLong", "" + longitude);

        } else {
            gpsTrack.showSettingsAlert();

            Log.e("ShowAlert", "ShowAlert");

        }

        countryCode = getAddress(PayPalPaymentGateway.this, latitude, longitude);

        Log.e("countryCode", "" + countryCode);

    }

    public String getAddress(Context ctx, double latitude, double longitude) {
        String region_code = null;
        try {

            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                region_code = address.getCountryCode();

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return region_code;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            Log.i("uri", uri.toString());
        }
    }

    private void setOnClickListeners() {
        findViewById(R.id.card).setOnClickListener(this);
        findViewById(R.id.paypal).setOnClickListener(this);
//        findViewById(R.id.weChat).setOnClickListener(this);
        findViewById(R.id.ali_pay).setOnClickListener(this);

    }

    private void makeTransaction(WirecardPaymentType wirecardPaymentType, WirecardTransactionType transactionType) {
        String currency;
        String merchantID;
        String secretKey;
        String signature;

        // for testing purposes only, do not store your merchant account ID and secret key inside app
        String timestamp = generateTimestamp();
        String requestID = UUID.randomUUID().toString();
        BigDecimal amount = new BigDecimal(10);

        if (code.matches("SG")) {
            currency = "SGD";
        } else if (code.matches("CN")) {
            currency = "CNY";
        } else {
            currency = "RMB";
        }

        WirecardPayment wirecardPayment = null;

        switch (wirecardPaymentType) {
            case CARD:

                merchantID = "33f6d473-3036-4ca5-acb5-8c64dac862d1";
                secretKey = "9e0130f6-2e1e-4185-b0d5-dc69079c75cc";

                signature = generateSignatureV2(timestamp, merchantID, requestID,
                        transactionType.getValue(), amount, currency, secretKey);

                wirecardPayment = new WirecardCardPayment(signature, timestamp, requestID,
                        merchantID, transactionType, amount, currency);
                ((WirecardCardPayment) wirecardPayment).setAttempt3d(true);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                    wirecardPayment.setProcessingURL("paymentsdk://web.result");
                break;
            case PAYPAL:

                merchantID = "9abf05c1-c266-46ae-8eac-7f87ca97af28";
                secretKey = "5fca2a83-89ca-4f9e-8cf7-4ca74a02773f";

                signature = generateSignatureV2(timestamp, merchantID, requestID,
                        transactionType.getValue(), amount, currency, secretKey);

                wirecardPayment = new WirecardPayPalPayment(signature, timestamp, requestID,
                        merchantID, transactionType, amount, currency);
                wirecardPayment.setProcessingURL("paymentsdk://web.result");
                break;
//            case SEPA:
//
//                merchantID = "4c901196-eff7-411e-82a3-5ef6b6860d64";
//                secretKey = "ecdf5990-0372-47cd-a55d-037dccfe9d25";
//
//                signature = generateSignatureV2(timestamp, merchantID, requestID,
//                        transactionType.getValue(), amount, currency, secretKey);
//
//                wirecardPayment = new WirecardSepaPayment(signature, timestamp, requestID,
//                        merchantID, transactionType, amount, currency, "creditorID", "mandateID", new Date(),
//                        "merchantName", null);
//                break;
//            case PBBA:
//                merchantID = "70055b24-38f1-4500-a3a8-afac4b1e3249";
//                secretKey = "4a4396df-f78c-44b9-b8a0-b72b108ac465";
//
//                currency = "GBP";
//                String zappTransactionType = "PAYMT";
//                String zappDeliveryType = "SERVICE";
//                String returnValue = "paymentsdkdemo://open.pbba";
//
//                signature = generateSignatureV2(timestamp, merchantID, requestID,
//                        transactionType.getValue(), amount, currency, secretKey);
//
//                wirecardPayment = new WirecardPBBAPayment(signature, timestamp, requestID, merchantID, transactionType, amount, currency, zappTransactionType, zappDeliveryType, returnValue);
//                wirecardPayment.setIpAddress("127.0.0.1");
//                break;
//            case ALIPAY:
//                Intent ali_payment=new Intent(PayPalPaymentGateway.this,PayDemoActivity.class);
//                startActivity(ali_payment);
//                break;

//                PayTask payTask = new PayTask(PayPalPaymentGateway.this);
//                String version = payTask.getVersion();
//
//                final Handler handler = new Handler() {
//                    @Override
//                    public void publish(LogRecord record) {
//
//                    }
//
//                    @Override
//                    public void flush() {
//
//                    }
//
//                    @Override
//                    public void close() throws SecurityException {
//
//                    }
//                    public void handleMessage(final Message msg) {
//                           Result result = new Result() {
//                            @Override
//                            public void setSystemId(String systemId) {
//
//                            }
//
//                            @Override
//                            public String getSystemId() {
//                                return (String)msg.obj;
//                            }
//
//                        };
//                        Toast.makeText(PayPalPaymentGateway.this, result.getSystemId(),
//                                Toast.LENGTH_LONG).show();
//                    }
//                };
//
//
//                      String info[] = {"","",""} ;
//               final String orderInfo = Arrays.toString(info);
//               // final String orderInfo = info;   // order information
//
//                Runnable payRunnable = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        PayTask alipay = new PayTask(PayPalPaymentGateway.this);
//                        String result = alipay.pay(orderInfo);
//
//                        Message msg = new Message();
//                        msg.what = 1;
//                        msg.obj = result;
//                        handler.sendMessage(msg);
//                    }
//                };
////                 must call asynchronously
//                Thread payThread = new Thread(payRunnable);
//                payThread.start();
        }

        wirecardClient.makePayment(wirecardPayment, null, new WirecardResponseListener() {
            @Override
            public void onResponse(WirecardPaymentResponse wirecardPaymentResponse) {
                dismissPbbaPopup();
                Log.d(de.wirecard.paymentsdk.BuildConfig.APPLICATION_ID, "response received");
//                resultLabel.setText(wirecardPaymentResponse.getTransactionState().getValue() + "\n"
//                        + wirecardPaymentResponse.getStatuses().getStatus()[0].getDescription());
            }

            @Override
            public void onError(WirecardResponseError wirecardResponseError) {
                dismissPbbaPopup();
                Log.d(de.wirecard.paymentsdk.BuildConfig.APPLICATION_ID, wirecardResponseError.getErrorMessage());
                //   resultLabel.setText(wirecardResponseError.getErrorMessage());
            }
        });
    }

    private void dismissPbbaPopup() {
        PBBAAppUtils.dismissPBBAPopup(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card:
                makeTransaction(WirecardPaymentType.CARD, WirecardTransactionType.PURCHASE);
                break;
            case R.id.paypal:
                Intent serviceConfig = new Intent(this, PayPalService.class);
                serviceConfig.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                startService(serviceConfig);

                PayPalPayment payment = new PayPalPayment(new BigDecimal("5.65"),
                        "USD", "My Awesome Item", PayPalPayment.PAYMENT_INTENT_SALE);

                Intent paymentConfig = new Intent(this, PaymentActivity.class);
                paymentConfig.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                paymentConfig.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(paymentConfig, 0);
//                makeTransaction(WirecardPaymentType.PAYPAL, WirecardTransactionType.DEBIT);
                break;

            case R.id.ali_pay:

                Intent ali_payment = new Intent(PayPalPaymentGateway.this, PayDemoActivity.class);
                startActivity(ali_payment);
                break;

//            case R.id.weChat:
//                PayReq request = new PayReq();
//                request.appId = "wxd930ea5d5a258f4f";
//                request.partnerId = "1900000109";
//                request.prepayId= "1101000000140415649af9fc314aa427";
//                request.packageValue = "Sign=WXPay";
//                request.nonceStr= "1101000000140429eb40476f8896f4c9";
//                request.timeStamp= "1398746574";
//                request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
//                api.sendReq(request);
//                final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
//
//// Register the app to WeChat
//
//                msgApi.registerApp("wxd930ea5d5a258f4f");
//                        //1.create request for wechat pay
//                        WechatPayReq wechatPayReq = new WechatPayReq.Builder()
//                        .with(this) //activity instance
//                        .setAppId(request.appId) //wechat pay AppID
//                        .setPartnerId(request.partnerId)//wechat pay partner id
//                        .setPrepayId(request.prepayId)//pre pay id
////								.setPackageValue(wechatPayReq.get)//"Sign=WXPay"
//                        .setNonceStr(request.nonceStr)
//                        .setTimeStamp(request.timeStamp)//time stamp
//                        .setSign("e8714228226295114a845b5d7c8f4b8d")//sign
//                        .create();
//                    //2. send the request with wechat pay
//                    PayAPI.getInstance().sendPayRequest(wechatPayReq);


            //set the callback for wechat pay
            //wechatPayReq.setOnWechatPayListener(new OnWechatPayListener);


//            case R.id.aliPay:
//                //step 1 create raw ali pay order info
              /*  String rawAliOrderInfo = new AliPayReq2.AliOrderInfo()
                        .setPartner(partner) //set partner
                        .setSeller(seller)  //set partner seller accout
                        .setOutTradeNo(outTradeNo) //set unique trade no
                        .setSubject(orderSubject) //set order subject
                        .setBody(orderBody) //set order detail
                        .setPrice(price) //set price
                        .setCallbackUrl(callbackUrl) //set callback for pay reqest
                        .createOrderInfo(); //create ali pay order info


                //step 2 get the signed ali pay order info
                String signAliOrderInfo = getSignAliOrderInfoFromServer(rawAliOrderInfo);

                //step 3 step 3 send the request for ali pay
                AliPayReq2 aliPayReq = new AliPayReq2.Builder()
                        .with(this)//Activity instance
                        .setSignedAliPayOrderInfo(signAliOrderInfo)
                        .setRawAliPayOrderInfo(rawAliOrderInfo)//set the ali pay order info
                        .setSignedAliPayOrderInfo(signAliOrderInfo) //set the signed ali pay order info
                        .create()//
                        .setOnAliPayListener(null);//
                PayAPI.getInstance().sendPayRequest(aliPayReq);


              //  set the ali pay callback
                aliPayReq.setOnAliPayListener(new OnAliPayListener);*/
//            case R.id.sepa:
//                makeTransaction(WirecardPaymentType.SEPA, WirecardTransactionType.PENDING_DEBIT);
//                break;
//            case R.id.pbba:
//                makeTransaction(WirecardPaymentType.PBBA, WirecardTransactionType.DEBIT);
//                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(
                    PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("sampleapp", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification

                } catch (JSONException e) {
                    Log.e("sampleapp", "no confirmation data: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("sampleapp", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("sampleapp", "Invalid payment / config set");
        }
    }

    private static String generateSignatureV2(String timestamp, String merchantID, String requestID,
                                              String transactionType, BigDecimal amount, String currency,
                                              String secretKey) {

        String payload = ENCRYPTION_ALGORITHM.toUpperCase() + "\n" +
                "request_time_stamp=" + timestamp + "\n" +
                "merchant_account_id=" + merchantID + "\n" +
                "request_id=" + requestID + "\n" +
                "transaction_type=" + transactionType + "\n" +
                "requested_amount=" + amount + "\n" +
                "requested_amount_currency=" + currency.toUpperCase();

        try {
            byte[] encryptedPayload = encryptSignatureV2(payload, secretKey);
            return new String(Base64.encode(payload.getBytes(UTF_8), Base64.NO_WRAP), UTF_8)
                    + "." + new String(Base64.encode(encryptedPayload, Base64.NO_WRAP), UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encryptSignatureV2(String payload, String secretKey) {
        try {
            final Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(), "HmacSHA256"));
            return mac.doFinal(payload.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[1];
    }

    private String generateTimestamp() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        return new StringBuilder(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH)
                .format(calendar.getTime()))
                .insert(22, ":")
                .toString();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

}
