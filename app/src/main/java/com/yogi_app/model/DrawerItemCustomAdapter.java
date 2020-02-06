package com.yogi_app.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yogi_app.R;
import com.yogi_app.activity.Calender;
import com.yogi_app.activity.GalleryActivity;
import com.yogi_app.activity.MainActivity;



import static com.yogi_app.activity.MainActivity.adapter;
import static com.yogi_app.activity.MainActivity.mDrawerLayout;



/**
 * Created by anupamchugh on 10/12/15.
 */
public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {

    Context mContext;
    int layoutResourceId;
    DataModel data[] = null;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, DataModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);
        final ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
        final TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);
        final RelativeLayout rel_lay= (RelativeLayout) listItem.findViewById(R.id.rel_lay);
        DataModel folder = data[position];
        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        Typeface mFont = Typeface.createFromAsset(getContext().getAssets(), "robotlight.ttf");

        textViewName.setTypeface(mFont);


        return listItem;
    }
}

