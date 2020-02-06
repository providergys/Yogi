package com.yogi_app.retroutility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.multidex.MultiDexApplication;

import com.yogi_app.interfaces.ApiCalls;
import com.yogi_app.utility.URLForApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by gys on 02-03-2017.
 */

public class MainApplication extends MultiDexApplication
{
    private static ApiCalls apiCalls;
    public static Retrofit retrofit;
    @Override
    public void onCreate() {
        super.onCreate();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(URLForApplication.URL)
                .client(client)
            .addConverterFactory(GsonConverterFactory.create())
                .build();
//        System.out.println("MainApplication...");
        apiCalls = retrofit.create(ApiCalls.class);

//        System.out.println("MainApplication...   responce  "+apiCalls);


    }
    public static ApiCalls getApiService(){

//        System.out.println("MainApplication...      getApiService.....");
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(150, TimeUnit.SECONDS).readTimeout(150, TimeUnit.SECONDS).addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(URLForApplication.URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//        System.out.println("MainApplication...");
        apiCalls = retrofit.create(ApiCalls.class);
//        System.out.println("MainApplication...   responce  "+apiCalls);
        return apiCalls;
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE ));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
