package com.yogi_app.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.yogi_app.R;
import com.yogi_app.activity.EngageWebView;
import com.yogi_app.activity.PayPalPaymentGateway;
import com.yogi_app.model.FaqResponse;

import java.util.List;


public class EngageusAdapter extends RecyclerView.Adapter<EngageusAdapter.MyViewHolder> {

    private Context context;
    private TextView engage_txt, address_txt, readmore_txt;
    private ImageView imgs_engages;
    private List<FaqResponse> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMain;
        Button donate_button;
        public MyViewHolder(View view) {
            super(view);

            llMain = (LinearLayout) view.findViewById(R.id.ll_main);
            donate_button = (Button)view.findViewById(R.id.donate_button);

        }
    }

    public EngageusAdapter(Context context, List<FaqResponse> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.engage_us_items, parent, false);

        engage_txt = (TextView) itemView.findViewById(R.id.engage_txt);
        address_txt = (TextView) itemView.findViewById(R.id.address_txt);
        readmore_txt = (TextView) itemView.findViewById(R.id.readmore_txt);
        imgs_engages = (ImageView) itemView.findViewById(R.id.imgs_engages);

        Typeface mFont = Typeface.createFromAsset(context.getAssets(), "robotlight.ttf");

        engage_txt.setTypeface(mFont);
        address_txt.setTypeface(mFont);
        readmore_txt.setTypeface(mFont);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FaqResponse item = list.get(position);
        engage_txt.setText(item.getTitle());
        address_txt.setText(item.getContent());
        Picasso.with(context).load(item.getImage()).into(imgs_engages);
        readmore_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EngageWebView.class);
                intent.putExtra("URL", item.getUrl());
                Log.e("LINK", item.getUrl());
                context.startActivity(intent);

            }
        });
        holder.donate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity((Activity) context)
                        .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Intent intent = new Intent(context, PayPalPaymentGateway.class);
                        context.startActivity(intent);
                        System.out.println("sdfdfnmsfddddddddd" + "checked");
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                    }

                }).check();



            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
