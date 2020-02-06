package com.yogi_app.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yogi_app.R;
import com.yogi_app.adapters.QuizLevelsAdapter;
import com.yogi_app.fragments.DharmaQuizFragment;
import com.yogi_app.model.QuizModel;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.ProgDialog;
import com.yogi_app.utility.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AndroidDev on 30-Oct-18.
 */

public class QuizLevelQuestions extends AppCompatActivity implements View.OnClickListener {
    List<QuizModel> list = new ArrayList<>();
    ProgDialog progDialog = new ProgDialog();
    RecyclerView recycler;
    Integer levelID;
    TinyDB tinyDB;
    TextView tvSubmit;
    int ques_id, option_id;
    boolean correctOrNot = false;
    int userID;
    ArrayList<Integer> quiz_id_options = new ArrayList<Integer>();
    ArrayList<int[]> option_idList = new ArrayList<int[]>();
    ArrayList<int[]> result_arrayList = new ArrayList<int[]>();
    ArrayList<QuizModel> submitList = new ArrayList<QuizModel>();
    int count=0 ;
    int total_ques;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dharma_quiz);
        tinyDB = new TinyDB(QuizLevelQuestions.this);
        userID = tinyDB.getInt("user_id");
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);

        try {
            levelID = Integer.valueOf(getIntent().getExtras().getString("LevelId"));
            Log.d("dfkjhgsu", " " + levelID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        recycler = (RecyclerView) findViewById(R.id.recycler);
        apiGetQuizList();

    }

    @Override
    public void onClick(View v) {
        apiSubmitQuiz();

    }

    private int[] newValue(Integer... values) {
        int[] result = new int[values.length];
        for (int i = 0; i < result.length; i++)
            result[i] = values[i];
        return result;
    }

    private void apiSubmitQuiz() {

        quiz_id_options.clear();
//        options_list.clear();
        QuizModel quizModelRequest = new QuizModel();
//        System.out.println("listdsjfhsjfh4" + option_idList.size());
        quizModelRequest.setUser_id(userID);
        quizModelRequest.setQuestionarrayList(option_idList);
        quizModelRequest.setTerm_id(levelID);
        quizModelRequest.setScore(count);
//        Log.d("countkjghfehy",""+count);
        quizModelRequest.setTotal_question(total_ques);
//        quizModelRequest.setQuestionarrayList();
        MainApplication.getApiService().submitQuizAnswers("text/plain", quizModelRequest).enqueue(new Callback<QuizModel>() {
            @Override
            public void onResponse(Call<QuizModel> call, Response<QuizModel> response) {

                QuizModel quizModel = response.body();
                if (response.body().getRespCode().matches("7018")) {

                    list = quizModel.getLevelListCategories();

                    for (int i = 0; i < list.size(); i++) {
                        quiz_id_options.add(list.get(i).getQuestion_id());
//                        options_list.add(list.get(i).getQuizOptionList().get(i).getOption_id());
                        System.out.println("sgddddddfgdg" + list.get(i).getQuestion_id());
                    }

                    //     System.out.println("dfssssssssssssssssssss 4"+quiz_id_options.size());
//                    System.out.println("dfssssssssssssssssssss 4"+options_list.size());
                    recycler.setLayoutManager(new LinearLayoutManager(QuizLevelQuestions.this));
                    recycler.setAdapter(new DharmaQuizAdapter(list, quiz_id_options, QuizLevelQuestions.this));
                    Log.d("listSistqdyqdf", "" + list.size());

                } else {
                    Toast.makeText(QuizLevelQuestions.this, "" + quizModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<QuizModel> call, Throwable t) {
                Toast.makeText(QuizLevelQuestions.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void apiGetQuizList() {

        quiz_id_options.clear();
//        options_list.clear();
        QuizModel quizModelRequest = new QuizModel();
        quizModelRequest.setTerm_id(levelID);
        MainApplication.getApiService().getQuizQuestions("text/plain", quizModelRequest).enqueue(new Callback<QuizModel>() {
            @Override
            public void onResponse(Call<QuizModel> call, Response<QuizModel> response) {

                QuizModel quizModel = response.body();
                if (response.body().getRespCode().matches("7018")) {

                    list = quizModel.getLevelListCategories();
                    total_ques = quizModel.getTotal_quiz();
                    Log.d("totffgkjgh",""+total_ques);
                    for (int i = 0; i < list.size(); i++) {
                        quiz_id_options.add(list.get(i).getQuestion_id());
//                        options_list.add(list.get(i).getQuizOptionList().get(i).getOption_id());
                        System.out.println("sgddddddfgdg" + list.get(i).getQuestion_id());
                    }

                    //     System.out.println("dfssssssssssssssssssss 4"+quiz_id_options.size());
//                    System.out.println("dfssssssssssssssssssss 4"+options_list.size());
                    recycler.setLayoutManager(new LinearLayoutManager(QuizLevelQuestions.this));
                    recycler.setAdapter(new DharmaQuizAdapter(list, quiz_id_options, QuizLevelQuestions.this));
                    Log.d("listSistqdyqdf", "" + list.size());

                } else {
                    Toast.makeText(QuizLevelQuestions.this, "" + quizModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<QuizModel> call, Throwable t) {
                Toast.makeText(QuizLevelQuestions.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public class DharmaQuizAdapter extends RecyclerView.Adapter<DharmaQuizAdapter.MyViewHolder> {
        private List<QuizModel> list;
        private Context context;
        ArrayList<Integer> quiz_id_options = new ArrayList<Integer>();

        public DharmaQuizAdapter(List<QuizModel> list, ArrayList<Integer> quiz_id_options, Context context) {
            this.list = list;
            this.context = context;
            this.quiz_id_options = quiz_id_options;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View QuestionsView = LayoutInflater.from(context).inflate(R.layout.item_dharma_quiz, parent, false);
            return new MyViewHolder(QuestionsView);

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
          final  QuizModel item;
            item = list.get(holder.getAdapterPosition());

            final MyViewHolder myViewHolder = (MyViewHolder) holder;

            myViewHolder.tvQuesNum.setText(String.valueOf(position+1));
            myViewHolder.tvQuestion.setText(item.getQuizQuestion());
            result_arrayList.add(newValue(4,4));


            try {
                myViewHolder.chkboxOption1.setText(item.getQuizOptionList().get(0).getOption());
                myViewHolder.chkboxOption2.setText(item.getQuizOptionList().get(1).getOption());
                myViewHolder.chkboxOption3.setText(item.getQuizOptionList().get(2).getOption());
                myViewHolder.chkboxOption4.setText(item.getQuizOptionList().get(3).getOption());
            } catch (Exception e) {

            }
            myViewHolder.chkboxOption1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        myViewHolder.chkboxOption1.setChecked(true);
                        myViewHolder.chkboxOption2.setChecked(false);
                        myViewHolder.chkboxOption3.setChecked(false);
                        myViewHolder.chkboxOption4.setChecked(false);
                        ques_id = quiz_id_options.get(position);
                        option_id = item.getQuizOptionList().get(0).getOption_id();
                        option_idList.add(newValue(ques_id, option_id));
                        correctOrNot = item.getQuizOptionList().get(0).getCorrectAnsORNot();
                        if(correctOrNot){
                            count++;
                        }
                        Toast.makeText(context, "kfg" + ques_id + " " + option_id +" " +correctOrNot, Toast.LENGTH_SHORT).show();
                    } else {
                        option_idList.clear();
                    }
                    System.out.println(quiz_id_options.get(position) + "dfssssssssssssssssssss 1" + item.getQuizOptionList().get(0).getOption_id());
                }
            });
            myViewHolder.chkboxOption2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        myViewHolder.chkboxOption2.setChecked(true);
                        myViewHolder.chkboxOption1.setChecked(false);
                        myViewHolder.chkboxOption3.setChecked(false);
                        myViewHolder.chkboxOption4.setChecked(false);
                        ques_id = quiz_id_options.get(position);
                        option_id = item.getQuizOptionList().get(1).getOption_id();
                        option_idList.add(newValue(ques_id, option_id));
                        correctOrNot = item.getQuizOptionList().get(1).getCorrectAnsORNot();
                        if(correctOrNot){
                            count++;

                        }
                        Toast.makeText(context, "kfg" + ques_id + " " + option_id +" " +correctOrNot, Toast.LENGTH_SHORT).show();
                    } else {
                        option_idList.clear();
                    }
                    System.out.println(quiz_id_options.get(position) + "dfssssssssssssssssssss 2" + item.getQuizOptionList().get(1).getOption_id());
                }
            });
            myViewHolder.chkboxOption3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        myViewHolder.chkboxOption3.setChecked(true);
                        myViewHolder.chkboxOption2.setChecked(false);
                        myViewHolder.chkboxOption1.setChecked(false);
                        myViewHolder.chkboxOption4.setChecked(false);
                        ques_id = quiz_id_options.get(position);
                        option_id = item.getQuizOptionList().get(2).getOption_id();
                        option_idList.add(newValue(ques_id, option_id));
                        correctOrNot = item.getQuizOptionList().get(2).getCorrectAnsORNot();
                        Toast.makeText(context, "kfg" + ques_id + " " + option_id +" " +correctOrNot, Toast.LENGTH_SHORT).show();
                        if(correctOrNot){
                            count++;
                        }
                    } else {
                        option_idList.clear();
                    }
                    System.out.println(quiz_id_options.get(position) + "dfssssssssssssssssssss 3" + item.getQuizOptionList().get(2).getOption_id());

                }
            });
            myViewHolder.chkboxOption4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        myViewHolder.chkboxOption4.setChecked(true);
                        myViewHolder.chkboxOption2.setChecked(false);
                        myViewHolder.chkboxOption3.setChecked(false);
                        myViewHolder.chkboxOption1.setChecked(false);
                        ques_id = quiz_id_options.get(position);
                        option_id = item.getQuizOptionList().get(3).getOption_id();
                        option_idList.add(newValue(ques_id, option_id));
                        correctOrNot = item.getQuizOptionList().get(3).getCorrectAnsORNot();
                        if(correctOrNot){
                            count++;
                        }
                        Toast.makeText(context, "kfg" + ques_id + " " + option_id +" " +correctOrNot, Toast.LENGTH_SHORT).show();
                    } else {
                        option_idList.clear();
                    }
                    System.out.println(quiz_id_options.get(position) + "dfssssssssssssssssssss 4" + item.getQuizOptionList().get(3).getOption_id());
                }
            });


        }


        @Override
        public int getItemCount() {
            return list.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvQuesNum, tvQuestion;
            CheckBox chkboxOption1, chkboxOption2, chkboxOption3, chkboxOption4;

            public MyViewHolder(View itemView) {
                super(itemView);

                tvQuesNum = (TextView) itemView.findViewById(R.id.tvQuesNo);
                tvQuestion = (TextView) itemView.findViewById(R.id.tvQuestion);

                chkboxOption1 = (CheckBox) itemView.findViewById(R.id.chkboxOption1);
                chkboxOption2 = (CheckBox) itemView.findViewById(R.id.chkboxOption2);
                chkboxOption3 = (CheckBox) itemView.findViewById(R.id.chkboxOption3);
                chkboxOption4 = (CheckBox) itemView.findViewById(R.id.chkboxOption4);


            }

        }

    }


}

