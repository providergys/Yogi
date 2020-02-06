package com.yogi_app.activity;


import android.content.Context;
import android.graphics.Movie;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.yogi_app.R;
import com.yogi_app.model.FaqResponse;

@Layout(R.layout.list_item)

public class ChildView {


    private static String TAG ="ChildView";
    @View(R.id.lblListHeader)
    TextView textViewChild;
//    @View(R.id.child_desc)
//    TextView textViewDesc;
//    @View(R.id.child_image)
//    ImageView childImage;
    private Context mContext;
    private FaqResponse movie;
    public ChildView(Context mContext, FaqResponse movie) {
        this.mContext = mContext;
        this.movie = movie;
    }
    @Resolve
    private void onResolve(){
        Log.d(TAG,"onResolve");
        textViewChild.setText(movie.getAnswer());
        //Glide.with(mContext).load(movie.getImageUrl()).apply(RequestOptions.circleCropTransform()).into(childImage);
    }
}
