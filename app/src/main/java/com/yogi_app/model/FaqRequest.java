
package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaqRequest {

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("id")
    @Expose
    private String id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcategoryId() {
        return categoryId;
    }

    public void setcategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
