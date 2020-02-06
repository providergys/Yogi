package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndroidDev on 30-Oct-18.
 */

public class QuizModel {
    @SerializedName("user_id")
    @Expose
    private Integer user_id;
    @SerializedName("term_id")
    @Expose
    private Integer term_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("RespCode")
    @Expose
    private String respCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("categories")
    @Expose
    private List<QuizModel> levelListCategories;
    @SerializedName("quiz_id")
    @Expose
    private int question_id;
    @SerializedName("quiz_title")
    @Expose
    private String quizQuestion;
    @SerializedName("quiz_question")
    @Expose
    private List<QuizModel> quizOptionList;
    @SerializedName("option")
    @Expose
    private String option;
    @SerializedName("option_id")
    @Expose
    private Integer option_id;
    @SerializedName("correct")
    @Expose
    private boolean correctAnsORNot;
    @SerializedName("questionarray")
    @Expose
    private List<int[]> questionarrayList= new ArrayList<>();
    @SerializedName("resultArray")
    @Expose
    private List<int[]>  resultArrayList;
    @SerializedName("answer_id")
    @Expose
    private String answer_id;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("total_question")
    @Expose
    private int total_question;
    @SerializedName("total_quiz")
    @Expose
    private int total_quiz;



    public List<QuizModel> getLevelListCategories() {
        return levelListCategories;
    }

    public void setLevelListCategories(List<QuizModel> levelListCategories) {
        this.levelListCategories = levelListCategories;
    }

    public Integer getTerm_id() {
        return term_id;
    }

    public void setTerm_id(Integer term_id) {
        this.term_id = term_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(String quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

    public List<QuizModel> getQuizOptionList() {
        return quizOptionList;
    }

    public void setQuizOptionList(List<QuizModel> quizOptionList) {
        this.quizOptionList = quizOptionList;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Integer getOption_id() {
        return option_id;
    }

    public void setOption_id(Integer option_id) {
        this.option_id = option_id;
    }

    public boolean getCorrectAnsORNot() {
        return correctAnsORNot;
    }

    public void setCorrectAnsORNot(boolean correctAnsORNot) {
        this.correctAnsORNot = correctAnsORNot;
    }

    public List<int[]> getQuestionarrayList() {
        return questionarrayList;
    }

    public void setQuestionarrayList(List<int[]> questionarrayList) {
        this.questionarrayList = questionarrayList;
    }

    public List<int[]>  getResultArrayList() {
        return resultArrayList;
    }

    public void setResultArrayList(List<int[]>  resultArrayList) {
        this.resultArrayList = resultArrayList;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public int getTotal_question() {
        return total_question;
    }

    public void setTotal_question(int total_question) {
        this.total_question = total_question;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public boolean isCorrectAnsORNot() {
        return correctAnsORNot;
    }

    public int getTotal_quiz() {
        return total_quiz;
    }

    public void setTotal_quiz(int total_quiz) {
        this.total_quiz = total_quiz;
    }
}
