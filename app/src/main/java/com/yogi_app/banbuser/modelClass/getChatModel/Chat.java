
package com.yogi_app.banbuser.modelClass.getChatModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("video_id")
    @Expose
    private String videoId;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("senderinfo")
    @Expose
    private Senderinfo senderinfo;
    @SerializedName("messages")
    @Expose
    private String messages;
    @SerializedName("format_time")
    @Expose
    private String formatTime;
    @SerializedName("format_date")
    @Expose
    private String formatDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Senderinfo getSenderinfo() {
        return senderinfo;
    }

    public void setSenderinfo(Senderinfo senderinfo) {
        this.senderinfo = senderinfo;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

}
