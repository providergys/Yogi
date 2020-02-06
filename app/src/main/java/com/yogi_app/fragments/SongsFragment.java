package com.yogi_app.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yogi_app.R;
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

import static com.facebook.FacebookSdk.getApplicationContext;


public class SongsFragment extends Fragment {

    RecyclerView rvSongs;
    RvSongsAdapter rvSongsAdapter;
    String mediaType, mediaID;
    TinyDB tinyDB;
    SnakeBaar snakeBaar = new SnakeBaar();
    RelativeLayout rle;
    ProgDialog prog = new ProgDialog();
    ArrayList<String> newList = new ArrayList<String>();
    public SongsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        rvSongs = (RecyclerView) view.findViewById(R.id.rv_songs);
        rle = (RelativeLayout) view.findViewById(R.id.rle);
        tinyDB = new TinyDB(getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvSongs.setLayoutManager(mLayoutManager);
        rvSongs.setItemAnimator(new DefaultItemAnimator());
        apiFetchSongs();
        //  setRetainInstance(false);
        return view;
    }



    public void apiFetchSongs() {
        prog.progDialog(getActivity());
        newList.clear();
        MainApplication.getApiService().fetchSongs("text/plain").enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                prog.hideProg();
                FaqResponse faqResponse = response.body();
                if (response.body().getRespCode().matches("2013")) {
                    //   Toast.makeText(getActivity(), "" + faqResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    rvSongsAdapter = new RvSongsAdapter(getActivity(), faqResponse.getSongsList());
                    rvSongs.setAdapter(rvSongsAdapter);

                    for (int i = 0; i <faqResponse.getSongsList().size() ; i++) {
                        newList.add(faqResponse.getSongsList().get(i).getMediaID());

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
    @Override
    public void onPause() {
        super.onPause();
        try {
            rvSongsAdapter.stopPlayer();
        }catch (Exception e){e.printStackTrace();}
    }
    public class RvSongsAdapter extends RecyclerView.Adapter<RvSongsAdapter.MyViewHolder> {

        private int playingPosition;
        private MyViewHolder playingHolder;
        Context context;
        TextView songs_header, songs_txt;
        List<FaqResponse> list;
        MediaPlayer mediaPlayer;
        FaqResponse item;

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            LinearLayout llMain;
            Button share_btn;
            ImageView imgPlay, imgPlaylist;

            public MyViewHolder(View view) {
                super(view);

                llMain = (LinearLayout) view.findViewById(R.id.ll_main);
                imgPlay = (ImageView) view.findViewById(R.id.imgPlay);
                imgPlay.setOnClickListener(this);
                imgPlaylist = (ImageView) view.findViewById(R.id.imgPlaylist);
                share_btn = (Button) view.findViewById(R.id.share_btn);
                songs_header = (TextView) itemView.findViewById(R.id.songs_header);
                songs_txt = (TextView) itemView.findViewById(R.id.songs_txt);
                if(tinyDB.getString("LOGIN").matches("user_login")){
                    imgPlaylist.setVisibility(View.VISIBLE);
                }else{
                    imgPlaylist.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onClick(View v) {
                if (getAdapterPosition() == playingPosition) {
                    // toggle between play/pause of audio
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.start();
                    }
                } else {
                    // start another audio playback
                    playingPosition = getAdapterPosition();
                    if (mediaPlayer != null) {
                        if (null != playingHolder) {
                            updateNonPlayingView(playingHolder);
                        }
                        mediaPlayer.release();
                    }
                    playingHolder = this;
                    startMediaPlayer(list.get(playingPosition).getLink());
                }
                updatePlayingView();
            }
        }


        private void startMediaPlayer(String url) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(url));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releaseMediaPlayer();
                }
            });
            mediaPlayer.start();
        }

        private void releaseMediaPlayer() {
            if (null != playingHolder) {
                updateNonPlayingView(playingHolder);
            }
            mediaPlayer.release();
            mediaPlayer = null;
            playingPosition = -1;
        }


        public RvSongsAdapter(Context context, List<FaqResponse> list) {
            this.context = context;
            this.list = list;
            this.playingPosition = -1;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.songs_items, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            mediaPlayer = new MediaPlayer();
            item = list.get(position);
            songs_txt.setText(item.getTitle());

            Typeface mFont = Typeface.createFromAsset(context.getAssets(), "robotlight.ttf");

            songs_header.setTypeface(mFont);
            songs_txt.setTypeface(mFont);


            if (position == playingPosition) {
                playingHolder = holder;
                try {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(item.getLink());
                } catch (Exception e) {

                }

                // this view holder corresponds to the currently playing audio cell
                // update its view to show playing progress
                updatePlayingView();
            } else {
                // and this one corresponds to non playing
                updateNonPlayingView(holder);
            }

            holder.share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, item.getLink());
                        context.startActivity(Intent.createChooser(i, "choose one"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            holder.imgPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaType = item.getType();
                  //  mediaID = item.getMediaID();
                    mediaID = newList.get(position);
                   // System.out.println(mediaID+"dufcjkhjdhdhgjghjdgj"+newList);
                    Log.d("DETAILSsss",""+mediaID +mediaType);
                    add_to_playlist_api();
                }
            });

        }


        @Override
        public void onViewRecycled(MyViewHolder holder) {
            super.onViewRecycled(holder);
            if (playingPosition == holder.getAdapterPosition()) {
                // view holder displaying playing audio cell is being recycled
                // change its state to non-playing
                updateNonPlayingView(playingHolder);
                playingHolder = null;
            }
        }

        private void updateNonPlayingView(MyViewHolder holder) {
            if (holder == playingHolder) {
                holder.imgPlay.setImageResource(R.drawable.audio_play_btn);

            }

        }

        private void updatePlayingView() {

            if (mediaPlayer.isPlaying()) {
                playingHolder.imgPlay.setImageResource(R.drawable.audio_pause_btn);
            } else {
                playingHolder.imgPlay.setImageResource(R.drawable.audio_play_btn);
            }
        }

        void stopPlayer() {
            if (null != mediaPlayer) {
                releaseMediaPlayer();
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }

    public void add_to_playlist_api() {
        final Integer userId = tinyDB.getInt("user_id");
        FaqResponse request = new FaqResponse();

        request.setType(mediaType);
        request.setUserId(userId);
        request.setMediaID(mediaID);

        MainApplication.getApiService().sendSelectedMediaFiles("text/plain", request).enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                FaqResponse faqResponse = response.body();
                if (response.body().getRespCode().matches("2023")) {
                    Log.d("DETAILSsss",""+userId +mediaID +mediaType);
                    snakeBaar.showSnackBar(getActivity(), "" + response.body().getMessage(), rle);
                } else {
                    snakeBaar.showSnackBar(getActivity(), "" + response.body().getMessage(), rle);
                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {

            }
        });
    }
}
