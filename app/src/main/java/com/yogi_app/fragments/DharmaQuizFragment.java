package com.yogi_app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.mindorks.placeholderview.ExpandablePlaceHolderView;
import com.yogi_app.R;
import com.yogi_app.activity.ChildView;
import com.yogi_app.activity.HeaderView;
import com.yogi_app.activity.Profile;
import com.yogi_app.adapters.ExpandlListAdapter;
import com.yogi_app.adapters.ProfileOrdinationAdapter;
import com.yogi_app.adapters.QuizLevelsAdapter;
import com.yogi_app.model.CalendarRequest;
import com.yogi_app.model.CalendarResponse;
import com.yogi_app.model.DharmaQuizModel;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.ProfileOrdination;
import com.yogi_app.model.QuizModel;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.ProgDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DharmaQuizFragment extends Fragment {

    List<QuizModel> list = new ArrayList<>();
    ProgDialog progDialog = new ProgDialog();
    RecyclerView recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quiz_levels, container, false);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
//        list = addQuizList();
        apiGetQuizLevel();
        return view;
    }

    private void apiGetQuizLevel() {

        MainApplication.getApiService().getQuizLevel("text/plain").enqueue(new Callback<QuizModel>() {
            @Override
            public void onResponse(Call<QuizModel> call, Response<QuizModel> response) {

                QuizModel quizModel = response.body();
                if (response.body().getRespCode().matches("2015")) {

                    list = quizModel.getLevelListCategories();
                    //  Toast.makeText(getActivity(), "" + list.size(), Toast.LENGTH_SHORT).show();

                    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recycler.setAdapter(new QuizLevelsAdapter(list, getActivity()));
                    Log.d("listSistqdyqdf", "" + list.size());

//                    String q =quizModel.getLevelListCategories().get(1).getTerm_id();
//                    String n =quizModel.getLevelListCategories().get(0).getName();
//                    Toast.makeText(getActivity(), "" + quizModel.getMessage() +" " +q+" "+n, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "" + quizModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<QuizModel> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}
