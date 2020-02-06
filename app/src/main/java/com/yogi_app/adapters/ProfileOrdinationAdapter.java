package com.yogi_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yogi_app.R;
import com.yogi_app.activity.EngageWebView;
import com.yogi_app.activity.PayPalPaymentGateway;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.ProfileOrdination;

import java.util.List;

/**
 * Created by AndroidDev on 24-Oct-18.
 */


public class ProfileOrdinationAdapter extends RecyclerView.Adapter<ProfileOrdinationAdapter.MyViewHolder> {

    private Context context;
    private List<FaqResponse> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox chkboxOrdination;
        TextView tvDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            chkboxOrdination = (CheckBox) itemView.findViewById(R.id.chkboxOrdination);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

        }
    }

    public ProfileOrdinationAdapter( List<FaqResponse> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_ordination, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        FaqResponse item = list.get(position);


        holder.chkboxOrdination.setText( item.getOrdinated_name());
        holder.tvDate.setText( item.getOrdinated_name());
//        ProfileOrdination item = list.get(position);
//        holder.chkboxOrdination.setText(item.getLevel());
//       // Toast.makeText(context,""+item.getLevel(),Toast.LENGTH_SHORT).show();
//        holder.tvDate.setText(item.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
