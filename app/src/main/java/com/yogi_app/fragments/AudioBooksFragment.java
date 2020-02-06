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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AudioBooksFragment extends Fragment {
    TextView audio_txt, audio_time;
    Typeface mFont;
    RecyclerView rvAudioBooks;
    RvAudioBooksAdapter rvAudioBooksAdapter;
    TinyDB tinyDB;
    String media_type,media_id;
    RelativeLayout frag1;
    List<FaqResponse> audioList = new ArrayList<>();
    SnakeBaar snakeBaar = new SnakeBaar();
    ProgDialog progDialog = new ProgDialog();
    public AudioBooksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_audio_book, container, false);
        tinyDB = new TinyDB(getActivity());
        rvAudioBooks = (RecyclerView) view.findViewById(R.id.rv_audio_books);
        frag1=(RelativeLayout)view.findViewById(R.id.frag1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvAudioBooks.setLayoutManager(mLayoutManager);
        rvAudioBooks.setItemAnimator(new DefaultItemAnimator());
        apiFetchAudio();
        return view;

    }

    public void apiFetchAudio() {
        progDialog.progDialog(getActivity());
        MainApplication.getApiService().fetchAudioBooks("text/plain").enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {

                FaqResponse faqResponse = response.body();
                if (response.body().getRespCode().matches("2013")) {

                    progDialog.hideProg();
                    audioList = faqResponse.getAudioBooksList();
                    rvAudioBooksAdapter = new RvAudioBooksAdapter(getActivity(), faqResponse.getAudioBooksList());
                    rvAudioBooks.setAdapter(rvAudioBooksAdapter);
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
            rvAudioBooksAdapter.stopPlayer();
        }catch (Exception e){}
    }

    public class RvAudioBooksAdapter extends RecyclerView.Adapter<RvAudioBooksAdapter.MyViewHolder>  implements Handler.Callback{
        private static final int MSG_UPDATE_SEEK_BAR = 1845;

        private MediaPlayer mediaPlayer;

        private Handler uiUpdateHandler;

        private int playingPosition;
        private MyViewHolder playingHolder;
        Context context;
        List<FaqResponse> list;


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

            ImageView ivPlayPause,imgPlaylist;
            Button btnShare;
            TextView audio_time;
            SeekBar sbProgress;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivPlayPause = (ImageView) itemView.findViewById(R.id.iv_music);
                ivPlayPause.setOnClickListener(this);
                sbProgress = (SeekBar) itemView.findViewById(R.id.seekBar2);
                sbProgress.setOnSeekBarChangeListener(this);
                btnShare = (Button) itemView.findViewById(R.id.share_btn);
                audio_txt = (TextView) itemView.findViewById(R.id.audio_txt);
                audio_time = (TextView) itemView.findViewById(R.id.audio_time);
                imgPlaylist = (ImageView) itemView.findViewById(R.id.imgPlaylist);
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

        public RvAudioBooksAdapter(Context context, List<FaqResponse> list) {
            this.context = context;
            this.list = list;
            this.playingPosition = -1;
            uiUpdateHandler = new Handler(this);
        }

        @Override
        public RvAudioBooksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.audio_books_items, parent, false);

            audio_time = (TextView) itemView.findViewById(R.id.audio_time);
            mFont = Typeface.createFromAsset(context.getAssets(), "robotlight.ttf");
            audio_time.setTypeface(mFont);
            return new RvAudioBooksAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RvAudioBooksAdapter.MyViewHolder holder, final int position) {

            final RvAudioBooksAdapter.MyViewHolder viewHolder = (RvAudioBooksAdapter.MyViewHolder) holder;
            final FaqResponse item = list.get(position);
            audio_txt.setText(item.getTitle());
            audio_txt.setTypeface(mFont);
            audio_time.setText(item.getLength());
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
                    } catch(Exception e) {
                        e.printStackTrace();
                    }




                }
            });


//            Image Playlist icon Click Listener...
            holder.imgPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    media_type=item.getType();
                    media_id=item.getMediaID();

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

    public void add_to_playlist_api(){
        Integer userId = tinyDB.getInt("user_id");
        FaqResponse request = new FaqResponse();

        request.setType(media_type);
        request.setUserId(userId);
        request.setMediaID(media_id);
        // Toast.makeText(getActivity(),"ID"+userId +media_type +request.getMediaID(),Toast.LENGTH_SHORT).show();

        MainApplication.getApiService().sendSelectedMediaFiles("text/plain",request).enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                FaqResponse faqResponse = response.body();
                if (response.body().getRespCode().matches("2023")) {
                    snakeBaar.showSnackBar(getActivity(),""+response.body().getMessage(),frag1);
                    //    Toast.makeText(getActivity(), "res  " + faqResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    //  Toast.makeText(getActivity(), "res else" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    snakeBaar.showSnackBar(getActivity(),""+response.body().getMessage(),frag1);
                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {

            }
        });
    }
}
