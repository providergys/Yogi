package com.yogi_app.fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.appunite.appunitevideoplayer.PlayerActivity;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;
import com.yogi_app.R;
import com.yogi_app.adapters.ExpandlListAdapter;
import com.yogi_app.adapters.RvVideoAdapter;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.ProgDialog;
import com.yogi_app.utility.SnakeBaar;
import com.yogi_app.utility.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VIdeoFragment extends Fragment {

    RecyclerView rvVideo;
    RvVideoAdapter rvVideoAdapter;
    TinyDB tinyDB;
    RelativeLayout lay_rel;
    SnakeBaar snakeBaar = new SnakeBaar();
    ProgDialog progDialog = new ProgDialog();
    ArrayList<String> newList = new ArrayList<String>();

    public VIdeoFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        rvVideo = (RecyclerView) view.findViewById(R.id.rv_video);
        tinyDB = new TinyDB(getActivity());
        lay_rel = (RelativeLayout) view.findViewById(R.id.lay_rel);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvVideo.setLayoutManager(mLayoutManager);
        rvVideo.setItemAnimator(new DefaultItemAnimator());
        apiFetchVideos();
        return view;
    }

    public void apiFetchVideos() {
        progDialog.progDialog(getActivity());
        newList.clear();
        MainApplication.getApiService().fetchVideos("text/plain").enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {

                FaqResponse faqResponse = response.body();
                if (response.body().getRespCode().matches("2013")) {
                    rvVideoAdapter = new RvVideoAdapter(getActivity(), faqResponse.getVideoList());
                    rvVideo.setAdapter(rvVideoAdapter);
                    progDialog.hideProg();
                    for (int i = 0; i <faqResponse.getVideoList().size() ; i++) {
                        newList.add(faqResponse.getVideoList().get(i).getMediaID());

                    }

                } else {

                    Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {

            }
        });
    }

    public class RvVideoAdapter extends RecyclerView.Adapter<RvVideoAdapter.MyViewHolder> {

        Context context;
        Typeface mFont;
        List<FaqResponse> list;
        String mediaType, mediaID;

        public RvVideoAdapter(Context context, List<FaqResponse> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_items, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final FaqResponse item = list.get(position);
            mFont = Typeface.createFromAsset(context.getAssets(), "robotlight.ttf");
            holder.video_title.setTypeface(mFont);

            holder.imgVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   Toast.makeText(context,""+list.size(),Toast.LENGTH_SHORT).show();
                    startActivity(PlayerActivity.getVideoPlayerIntent(context, item.getLink(), "0"));
                }
            });

            holder.imgPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaType = item.getType();
                    mediaID = newList.get(position);
                    System.out.println(mediaID+"dufcjkhjdhdhgjghjdgj"+newList);
                    add_to_playlist_api();
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
            ImageView imgVideo ,imgPlaylist;

            public MyViewHolder(View itemView) {
                super(itemView);
                video_title = (TextView) itemView.findViewById(R.id.video_title);
                imgVideo = (ImageView) itemView.findViewById(R.id.imgVideo);
                imgPlaylist = (ImageView) itemView.findViewById(R.id.imgPlaylist);
                if(tinyDB.getString("LOGIN").matches("user_login")){
                    imgPlaylist.setVisibility(View.VISIBLE);
                }else{
                    imgPlaylist.setVisibility(View.INVISIBLE);
                }
            }
        }

        public void add_to_playlist_api() {
            Integer userId = tinyDB.getInt("user_id");
            FaqResponse request = new FaqResponse();
            request.setType(mediaType);
            request.setUserId(userId);
            request.setMediaID(mediaID);

            MainApplication.getApiService().sendSelectedMediaFiles("text/plain", request).enqueue(new Callback<FaqResponse>() {
                @Override
                public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                    FaqResponse faqResponse = response.body();
                    if (response.body().getRespCode().matches("2023")) {
                        snakeBaar.showSnackBar(getActivity(), "" + response.body().getMessage(), lay_rel);

                    } else {

                        snakeBaar.showSnackBar(getActivity(), "" + response.body().getMessage(), lay_rel);
                    }
                }

                @Override
                public void onFailure(Call<FaqResponse> call, Throwable t) {

                }
            });
        }
    }


}
