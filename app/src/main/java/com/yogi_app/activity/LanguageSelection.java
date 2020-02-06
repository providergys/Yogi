package com.yogi_app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yogi_app.R;

import java.util.Locale;

/**
 * Created by AndroidDev on 11-Oct-18.
 */

public class LanguageSelection  extends AppCompatActivity  implements View.OnClickListener{
    private Locale myLocale;
    private TextView txt_hello;
    private Button btn_en, btn_ru, btn_fr, btn_de;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_layout);
    }

    public void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }
    public void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }
    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }
    private void updateTexts()
    {
        txt_hello.setText("hello_world");
        btn_en.setText("btn_en");
        btn_ru.setText("btn_ru");
        btn_fr.setText("btn_fr");
        btn_de.setText("btn_de");
    }
    @Override
    public void onClick(View v) {
        String lang = "zh";
        switch (v.getId()) {
            case R.id.btn_en:
                lang = "zh";
                break;
            case R.id.btn_ru:
                lang = "ru";
                break;
            case R.id.btn_de:
                lang = "de";
                break;
            case R.id.btn_fr:
                lang = "fr";
                break;
            default:
                break;
        }
        changeLang(lang);
    }
    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }
}
