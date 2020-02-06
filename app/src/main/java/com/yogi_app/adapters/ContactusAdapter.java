package com.yogi_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yogi_app.R;
import com.yogi_app.activity.MailingList;
import com.yogi_app.model.FaqRequest;
import com.yogi_app.model.FaqResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ContactusAdapter extends RecyclerView.Adapter< ContactusAdapter.MyViewHolder> {

    Context context;
    TextView country_txt,address_txt,phone_no_txt;
    ImageView imgs_flags;
    List<FaqResponse> list ;
    ArrayList<Integer> flags_list=new ArrayList<Integer>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMain;
//        @Bind(R.id.country_txt)
//        TextView country_txt;
//        @Bind(R.id.address_txt)
//        TextView address_txt;
//        @Bind(R.id.phone_no_txt)
//        TextView phone_no_txt;

        public MyViewHolder(View view) {
            super(view);
          //    ButterKnife.bind(this,itemView);
              llMain = (LinearLayout)view.findViewById(R.id.ll_main );
              phone_no_txt = (TextView) view.findViewById(R.id.phone_no_txt );
              country_txt = (TextView) view.findViewById(R.id.country_txt );
              address_txt = (TextView) view.findViewById(R.id.address_txt );
             imgs_flags= (ImageView) itemView.findViewById(R.id.imgs_flags );

        }
    }

    public ContactusAdapter(Context context , List<FaqResponse> list) {
        this.list = list;
        this.context    = context;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.contact_us_items, parent, false);
//        flags_list.add(R.drawable.malaysia);
//        flags_list.add(R.drawable.singapore);
//        flags_list.add(R.drawable.indonesia);
//        flags_list.add(R.drawable.china);

//        country_txt = (TextView) itemView.findViewById(R.id.country_txt );
//        address_txt= (TextView) itemView.findViewById(R.id.address_txt );
//        phone_no_txt= (TextView) itemView.findViewById(R.id.phone_no_txt );



        return  new  MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FaqResponse item = list.get(position);

        country_txt.setText(item.getContactName());
        address_txt.setText(item.getContactEmail());
        phone_no_txt.setText(item.getContactPhone());
        Picasso.with(context).load(item.getContact_image()).into(imgs_flags);
        phone_no_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", item.getContactPhone(), null));
                context.startActivity(intent);
            }
        });
        address_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ item.getContactEmail()});
                context.startActivity(Intent.createChooser(emailIntent, "Send email using..."));
//                Intent intent = new Intent(context, MailingList.class);
//                context.startActivity(intent);
            }
        });

//        Typeface mFont = Typeface.createFromAsset(context.getAssets(), "robotlight.ttf");

//        country_txt.setTypeface(mFont);
//        address_txt.setTypeface(mFont);
//        phone_no_txt.setTypeface(mFont);
//        phone_no_txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(Intent.ACTION_CALL);
////
////                intent.setData(Uri.parse("tel:" +phone_no_txt.getText().toString()));
////                context.startActivity(intent);
//            }
//        });
        }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
