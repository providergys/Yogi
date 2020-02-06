package com.yogi_app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.facebook.FacebookSdk.getApplicationContext;

public class AudioPlayList extends Fragment {
    TextView audio_txt, audio_time;
    Typeface mFont;
    RecyclerView rvAudioBooks;
    AudioBooksAdapter audioBooksAdapter;
    List<FetchPlaylistResponse> audioList = new ArrayList<>();

    public AudioPlayList() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_book, container, false);
        rvAudioBooks = (RecyclerView) view.findViewById(R.id.rv_audio_books);
        if( MyPlaylist.audioList==null) {

        }else {
            audioList = MyPlaylist.audioList;
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rvAudioBooks.setLayoutManager(mLayoutManager);
            rvAudioBooks.setItemAnimator(new DefaultItemAnimator());
            audioBooksAdapter = new AudioPlayList.AudioBooksAdapter(getActivity(), audioList);
            rvAudioBooks.setAdapter(audioBooksAdapter);
        }

        return view;

    }


    public class AudioBooksAdapter extends RecyclerView.Adapter<AudioBooksAdapter.MyViewHolder> implements Handler.Callback{
        private static final int MSG_UPDATE_SEEK_BAR = 1845;

        private MediaPlayer mediaPlayer;

        private Handler uiUpdateHandler;

        private int playingPosition;
        private MyViewHolder playingHolder;
        Context context;
        List<FetchPlaylistResponse> list;


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{
            ImageView ivPlayPause,imgPlaylist;
            Button btnShare;
            TextView audio_time;
            SeekBar sbProgress;

            public MyViewHolder(View view) {
                super(view);
                ivPlayPause = (ImageView) itemView.findViewById(R.id.iv_music);
                ivPlayPause.setOnClickListener(this);
                sbProgress = (SeekBar) itemView.findViewById(R.id.seekBar2);
                sbProgress.setOnSeekBarChangeListener(this);
                btnShare = (Button) itemView.findViewById(R.id.share_btn);
                audio_txt = (TextView) itemView.findViewById(R.id.audio_txt);
                audio_time = (TextView) itemView.findViewById(R.id.audio_time);
                imgPlaylist = (ImageView) itemView.findViewById(R.id.imgPlaylist);

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
                    Log.d("mzjdsgfysjdufgytuj",""+list.get(playingPosition).getLink());
                }
                updatePlayingView();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
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


        public AudioBooksAdapter(Context context, List<FetchPlaylistResponse> list) {
            this.context = context;
            this.list = list;
            this.playingPosition = -1;
            uiUpdateHandler = new Handler(this);
        }

        @Override
        public AudioBooksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.audio_books_items, parent, false);

            audio_time = (TextView) itemView.findViewById(R.id.audio_time);
            mFont = Typeface.createFromAsset(context.getAssets(), "robotlight.ttf");
            audio_time.setTypeface(mFont);
            return new AudioBooksAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final AudioBooksAdapter.MyViewHolder holder, final int position) {

            final AudioBooksAdapter.MyViewHolder viewHolder = (AudioBooksAdapter.MyViewHolder) holder;
            final FetchPlaylistResponse item = list.get(position);
            audio_txt.setText(item.getTitle());
            audio_txt.setTypeface(mFont);
            audio_time.setText(item.getLength());
            holder.imgPlaylist.setVisibility(View.GONE);
            if (position == playingPosition) {
                playingHolder = holder;
                // this view holder corresponds to the currently playing audio cell
                // update its view to show playing progress
                updatePlayingView();
            } else {
                // and this one corresponds to non playing
                updateNonPlayingView(holder);
            }
            holder.btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog disl = new Dialog(context);
                    disl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    disl.setContentView(R.layout.share_dialogue);

                    try {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, item.getLink());
                        startActivity(Intent.createChooser(i, "choose one"));
                    } catch (Exception e) {
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
                uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
            }
            holder.sbProgress.setEnabled(false);
            holder.sbProgress.setProgress(0);
            holder.ivPlayPause.setImageResource(R.drawable.audio_play_btn);
        }

        private void updatePlayingView() {
            playingHolder.sbProgress.setMax(mediaPlayer.getDuration());
            playingHolder.sbProgress.setProgress(mediaPlayer.getCurrentPosition());
            playingHolder.sbProgress.setEnabled(true);
            if (mediaPlayer.isPlaying()) {
                uiUpdateHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 100);
                playingHolder.ivPlayPause.setImageResource(R.drawable.audio_pause_btn);
            } else {
                uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
                playingHolder.ivPlayPause.setImageResource(R.drawable.audio_play_btn);
            }
        }

        void stopPlayer() {
            if (null != mediaPlayer) {
                releaseMediaPlayer();
            }
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_SEEK_BAR: {
                    playingHolder.sbProgress.setProgress(mediaPlayer.getCurrentPosition());
                    uiUpdateHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 100);
                    return true;
                }
            }
            return false;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }

}
