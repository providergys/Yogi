package com.yogi_app.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appunite.appunitevideoplayer.PlayerActivity;
import com.yogi_app.R;
import com.yogi_app.fragments.VIdeoFragment;
import com.yogi_app.model.FaqResponse;

import java.util.List;

public class RvVideoAdapter extends RecyclerView.Adapter<RvVideoAdapter.MyViewHolder> {

    Context context;
    Typeface mFont;
    List<FaqResponse> list;

    public RvVideoAdapter(Context context,List<FaqResponse> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RvVideoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.video_items, parent, false );

        return new RvVideoAdapter.MyViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final FaqResponse item = list.get(position);
        mFont = Typeface.createFromAsset(context.getAssets(), "robotlight.ttf");
        holder.video_title.setTypeface(mFont);
        holder.video_title.setText(item.getTitle());

        holder.imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    context.startActivity(PlayerActivity.getVideoPlayerIntent(context, item.getLink(), item.getTitle()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMain;
        TextView video_title;
        ImageView imgVideo;
        public MyViewHolder(View itemView) {
            super(itemView);
            video_title=(TextView) itemView.findViewById(R.id.video_title);
            imgVideo = (ImageView)itemView.findViewById(R.id.imgVideo);
        }
    }
}