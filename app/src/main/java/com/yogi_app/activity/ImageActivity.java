package com.yogi_app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yogi_app.R;

public class ImageActivity extends AppCompatActivity {

    ImageView imageView;
    String link="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);
        imageView = (ImageView)findViewById(R.id.imgView);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           link  = bundle.getString("DOCURL");
        }
        Picasso.with(ImageActivity.this).load(link).into(imageView);
    }
}
