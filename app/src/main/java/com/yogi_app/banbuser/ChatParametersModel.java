package com.yogi_app.banbuser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatParametersModel {

    @SerializedName("sender_id")
    @Expose
    private String sender_id;
    @SerializedName("video_id")
    @Expose
    private String video_id;
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
}
