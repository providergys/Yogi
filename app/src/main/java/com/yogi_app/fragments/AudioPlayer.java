package com.yogi_app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yogi_app.R;
import com.yogi_app.activity.MyPlaylist;
import com.yogi_app.model.FetchPlaylistResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndroidDev on 13-Nov-18.
 */

public class AudioPlayer extends Fragment {
    RecyclerView rvAudioBooks;
    List<FetchPlaylistResponse> audioList = new ArrayList<>();
    AudioPlayList.AudioBooksAdapter audioBooksAdapter;

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
//            audioBooksAdapter = new AudioPlayList.AudioBooksAdapter(getActivity(), audioList);
            rvAudioBooks.setAdapter(audioBooksAdapter);
        }

        return view;

    }
}
