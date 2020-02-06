package com.yogi_app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.yogi_app.R;
import com.yogi_app.activity.MyPlaylist;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.FetchPlaylistResponse;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SongsPlaylist extends Fragment{

    RecyclerView rvSongs;
    SongsAdapter songsAdapter;
    List<FetchPlaylistResponse> songsList = new ArrayList<>();
    public SongsPlaylist() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view       = inflater.inflate( R.layout.fragment_songs, container, false );
        rvSongs         = (RecyclerView) view.findViewById(R.id.rv_songs);
        songsList = MyPlaylist.songsList;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager( getActivity() );
        rvSongs.setLayoutManager( mLayoutManager );
        rvSongs.setItemAnimator( new DefaultItemAnimator());
        if( MyPlaylist.songsList==null){
          //  Toast.makeText(SongsPlaylist.this, ""+songsList.size(),Toast.LENGTH_SHORT).show();
        }else {
            songsList = MyPlaylist.songsList;
            Log.e("SONGS",""+songsList.size());
            songsAdapter = new SongsAdapter( getActivity() , songsList);
            rvSongs.setAdapter( songsAdapter );

        }
        return view;
    }

    public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {


        private int playingPosition;
        private MyViewHolder playingHolder;
        FetchPlaylistResponse item;
        Context context;
        TextView songs_header, songs_txt;
        List<FetchPlaylistResponse> list;
        MediaPlayer mediaPlayer;
        boolean song_is_playing = false;


        public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
            LinearLayout llMain;
            Button share_btn;
            ImageView imgPlay,imgPlaylist;
            public MyViewHolder(View view) {
                super(view);

                llMain = (LinearLayout) view.findViewById(R.id.ll_main);
                imgPlay = (ImageView) view.findViewById(R.id.imgPlay);
                imgPlay.setOnClickListener(this);
                imgPlaylist = (ImageView) view.findViewById(R.id.imgPlaylist);
                imgPlaylist.setVisibility(View.GONE);
                share_btn = (Button) view.findViewById(R.id.share_btn);
                songs_header = (TextView) itemView.findViewById(R.id.songs_header);
                songs_txt = (TextView) itemView.findViewById(R.id.songs_txt);

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
        public SongsAdapter(Context context, List<FetchPlaylistResponse> list) {
            this.context = context;
            this.list = list;
            this.playingPosition = -1;
        }

        @Override
        public SongsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.songs_items, parent, false);


            return new SongsAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final SongsAdapter.MyViewHolder holder, final int position) {
            mediaPlayer = new MediaPlayer();
            final FetchPlaylistResponse item =list.get(position);
            songs_txt.setText(item.getTitle());
            if (position == playingPosition) {
                playingHolder = holder;
                try {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(item.getLink());
                }catch (Exception e){

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
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

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

            }

            holder.imgPlay.setImageResource(R.drawable.audio_play_btn);
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


}
