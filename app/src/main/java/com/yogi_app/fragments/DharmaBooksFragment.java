package com.yogi_app.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yogi_app.R;
import com.yogi_app.adapters.RvVideoAdapter;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.RvDramaBooksAdapter;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.ProgDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DharmaBooksFragment extends Fragment {

    RecyclerView rvDharmaBooks;
    TextView txt;
    Typeface mFont;
    RvDramaBooksAdapter rvDramaBooksAdapter;
    ProgDialog progDialog = new ProgDialog();

    public DharmaBooksFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dharma_books, container, false);

        rvDharmaBooks = (RecyclerView) view.findViewById(R.id.rv_dharma_books);
        txt = (TextView) view.findViewById(R.id.txt);
        mFont = Typeface.createFromAsset(getActivity().getAssets(), "robotlight.ttf");
        txt.setTypeface(mFont);

        int numberOfColumns = 4;
        rvDharmaBooks.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        apiFetchDharmaBooks();
        return view;
    }

    public void apiFetchDharmaBooks() {
        progDialog.progDialog(getActivity());
        MainApplication.getApiService().fetchDharmaBooks("text/plain").enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                progDialog.hideProg();
                FaqResponse faqResponse = response.body();
                if (response.body().getRespCode().matches("2013")) {
                    rvDramaBooksAdapter = new RvDramaBooksAdapter(getActivity(), faqResponse.getDharmaBooksList());
                    rvDharmaBooks.setAdapter(rvDramaBooksAdapter);
                } else {
                    Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {


            }
        });
    }
}
