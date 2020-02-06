package com.yogi_app.alipay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.yogi_app.R;
import com.yogi_app.activity.GPSTracker;
import com.yogi_app.activity.PayPalPaymentGateway;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PayDemoActivity extends FragmentActivity {
	GPSTracker gpsTrack;
	public static double latitude = 0;
	public static double longitude = 0;
	String countryCode, code;
	// 商户PID
	//partner PID
	public static final String PARTNER = "2088101122136241";
	// 商户收款账号
	//Seller's Alipay account 
	public static final String SELLER = "abc@gmail.com";
	// 商户私钥，pkcs8格式
	//RSA private key of merchant,pkcs8 format
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM68nDOVf+2StgB1AGd4Ug3NlHZ0l7D1IJMIhbgqwmiCTbG7TBfpNecthBq6Drbx4Y/p8jsmAGYlfzO+hBqZw7VjEd8sqD1Nt5sn/nygrUavzAMFn6yCRSXMOfOfZphEZOb/nqPo5SIbSmgBs3V+UD9JoXSWHH0sYaydlJpR1a8ZAgMBAAECgYBENbi30FCoGurP1cqvWOSBx11/g9J0wTvhJ0OvUvRXtP5bcLeXgAuX3c2jX9XxCHdqmz6fw1cIXMDOWsKNYERQobGxyrt5xZmnV0GlnNjlCBYi1rri9D9PNIJnd2sWVaMAMmGnxCXngpZvjyzKJM90xXxGtJsQNl3zqwuGGmuxfQJBAOg7PMtTSKbm9Jsb26qxSbL5A1M+xTZ6nAuvegB6+UvaTleKDVJZr6z1K1ofHiP3HW3pW8YXajoHAxbtlQtk35MCQQDj5WFXmMngIriPE19akIZby/GJ5D1v06MaIhOv3h2GGWpEEHFYGLqPypLuii3rmMXtXvH1I3q2bASkDw/oTyojAkEAyGZl/eduqGhg6IDPvKqkuIbd8bYXJP4FLqhMlaGJA4XtWOlOuaOfT5d5w5lavxp+ENzxTy3hgxWN+vkmRuDTdQJBAIDv63YbDMSSAGd6p21e7ZWMOpkwmA3n6JTFiOvsuDmBsZzWzLnyK8Nk8mKhrT9pjToyiKSQMUJ6tNl5aB+ggo8CQDPeZLhPA+YZFQRiuOrJXQBkyi0tNqtomx5xZ+VumCIMRGQJdMG/wy0tR8cD55bVV81VUSTIPDvR4Wc7IkxyoiE=";
	// 支付宝公钥
	//Alipay public key
	public static final String RSA_PUBLIC = "";
	private static final int SDK_PAY_FLAG = 1;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://global.alipay.com/service/app/9) 建议商户依赖异步通知
				 * Logics verification of synchronous results must be processed at server（For rules pls check https://global.alipay.com/service/app/9)
				 * It is strongly recommended that sellers should directly check the asynchronous notification at server and neglect the synchronous response
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息The info needs to be verified from synchronous results

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				// check whether the resultStatus is "9000" which demonstrate paid succeed,for detailed info of the codes returned pls check in the documents
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PayDemoActivity.this, "支付成功Paid succeed", Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					//If the resultStatus is not "9000" the result might be failure.
					//"8000" means under processing due to the payment channel or system (payment might have been made successfully), please check the payment status returns from Alipay async notifications. (Status of small probability)
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayDemoActivity.this, "支付结果确认中Under processing", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						//You can consider other codes as failure,including user cancel the payment or system error etc.
						Toast.makeText(PayDemoActivity.this, "支付失败Failure", Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
		gpsTrack = new GPSTracker(PayDemoActivity.this);
		try {
			TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
			code = tm.getNetworkCountryIso();
			Log.d("Countrycodiewwe",""+code);
		}catch (Exception e){
			e.printStackTrace();

		}
		if (gpsTrack.canGetLocation()) {
			latitude = gpsTrack.getLatitude();
			longitude = gpsTrack.getLongitude();

			Log.e("GPSLat", "" + latitude);
			Log.e("GPSLong", "" + longitude);

		} else {
			gpsTrack.showSettingsAlert();

			Log.e("ShowAlert", "ShowAlert");

		}

		countryCode = getAddress(PayDemoActivity.this, latitude, longitude);

		Log.e("countryCode", ""+countryCode);

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
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
			new AlertDialog.Builder(this).setTitle("警告alert").setMessage("需要配置Pls configure PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							finish();
						}
					}).show();
			return;
		}
		String orderInfo = getOrderInfo("测试的商品product", "该测试商品的详细描述description body", "0.01");

		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 * Pay special attention,this signature logic needs to be done on sever side,don't reveal you private key in the code of client!
		 */
		String sign = sign(orderInfo);
		try {
			/**
			 * 仅需对sign 做URL编码
			 * Only sign needs to be URL encoded
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 * The order info including the integral Alipay parameters
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				//Construct object:PayTask
				PayTask alipay = new PayTask(PayDemoActivity.this);
				// 调用支付接口，获取支付结果
				//call the pay interface ,get the payment result
				String result = alipay.pay(payInfo,true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		//asynchronous call
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 原生的H5（手机网页版支付切native支付） 【对应页面网页支付按钮】
	 * Native H5(From app website to native )【Correspondent pay button on website】
	 * 
	 * @param v
	 */
	public void h5Pay(View v) {
		Intent intent = new Intent(this, H5PayDemoActivity.class);
		Bundle extras = new Bundle();
		/**
		 * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
		 * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
		 * 商户可以根据自己的需求来实现
		 * url is the test website，it opens based on webview in app，the webview in demo is H5PayDemoActivity，
		 * The logic to intercept the url to pay is achieved by function shouldOverrideUrlLoading in H5PayDemoActivity in demo，
		 * Merchants can realize according to their own requirements 
		 */
		String url = "http://m.taobao.com";
		// url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
		//url can be the wap site of shopping site like taobao etc,during the payment process ,Alipay sdk intercept and pay
		extras.putString("url", url);
		intent.putExtras(extras);
		startActivity(intent);
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		//parter ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		//Seller's Alipay account 
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		//The unique transanction id in your system
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		//product name
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		//detailed product info
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		//specifies the foreign price of the items
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		//Async notification page 
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

		// 服务接口名称， 固定值
		//service name,fixed,no need to modify
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		//payment type,fixed,no need to modify
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		//charset,fixed,no need to modify
		orderInfo += "&_input_charset=\"utf-8\"";


		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		// set the overtime of non-payment transaction
		// the default value is 30m ，The transaction will be closed automatically once the time is up.
		// Range of values：1m～15d。
		// m-minute, h-hour, d-day, 1c-current day (Whenever the transaction is created, it will be closed at 0:00).
		// Demical point of the numerical value of this parameter is rejected, for example, 1.5h can be converted into 90m.
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		//Token (including account information) returned by open platform (authorization token, a right for merchant to access to some services of Alipay within a specified period).
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// The page redirected to after the payment，nullable
		orderInfo += "&return_url=\"m.alipay.com\"";
		if(countryCode.matches("SG")){
			orderInfo += "&currency=\"SGD\"";
		}else if(countryCode.matches("CN")){
			orderInfo += "&currency=\"CNY\"";
		}else{
			orderInfo += "&currency=\"RMB\"";
		}
		//global pay special parameters

		orderInfo += "&forex_biz=\"FP\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
