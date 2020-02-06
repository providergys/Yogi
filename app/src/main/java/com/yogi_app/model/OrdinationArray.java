
package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdinationArray {

    @SerializedName("term_id")
    @Expose
    private Integer termId;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("total_question")
    @Expose
    private Integer totalQuestion;
    @SerializedName("submit_date")
    @Expose
    private String submitDate;
    @SerializedName("term_name")
    @Expose
    private String termName;

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(Integer totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

}
