package com.yogi_app.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import com.yogi_app.activity.MyPlaylist;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.FetchPlaylistResponse;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VedioPlaylist  extends Fragment {

    RecyclerView rvVideo;
    VideoAdapter rvVideoAdapter;
    TinyDB tinyDB;
    List<FetchPlaylistResponse> vedioList = new ArrayList<>();
    public VedioPlaylist() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view       = inflater.inflate( R.layout.fragment_video, container, false );
        rvVideo         = (RecyclerView) view.findViewById(R.id.rv_video);
        tinyDB = new TinyDB(getActivity());
        if( MyPlaylist.vedioList==null) {

        } else {
            vedioList = MyPlaylist.vedioList;
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity() );
            rvVideo.setLayoutManager(mLayoutManager);
            rvVideo.setItemAnimator(new DefaultItemAnimator());
            rvVideoAdapter = new VideoAdapter( getActivity() ,vedioList);
            rvVideo.setAdapter( rvVideoAdapter );
        }

        return view;
    }

    public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

        Context context;
        Typeface mFont;
        List<FetchPlaylistResponse> list;
        ImageView imgPlaylist;
        String mediaType,mediaID;

        public VideoAdapter(Context context,List<FetchPlaylistResponse> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public VideoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.video_items, parent, false );
            return new VideoAdapter.MyViewHolder( itemView );
        }

        @Override
        public void onBindViewHolder(final VideoAdapter.MyViewHolder holder, final int position) {
            final FetchPlaylistResponse item = list.get(position);
            mFont = Typeface.createFromAsset(context.getAssets(), "robotlight.ttf");
            holder.video_title.setTypeface(mFont);
            imgPlaylist.setVisibility(View.GONE);

            holder.imgVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context,""+list.size(),Toast.LENGTH_SHORT).show();

                        startActivity(PlayerActivity.getVideoPlayerIntent(context, item.getLink(), "0"));
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
                imgPlaylist = (ImageView)itemView.findViewById(R.id.imgPlaylist);
            }
        }


    }



}
