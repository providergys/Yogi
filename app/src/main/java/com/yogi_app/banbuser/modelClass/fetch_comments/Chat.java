
package com.yogi_app.banbuser.modelClass.fetch_comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("messages")
    @Expose
    private String messages;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("video_id")
    @Expose
    private String videoId;
    @SerializedName("format_time")
    @Expose
    private String formatTime;
    @SerializedName("format_date")
    @Expose
    private String formatDate;
    @SerializedName("senderinfo")
    @Expose
    private Senderinfo senderinfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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

    public Senderinfo getSenderinfo() {
        return senderinfo;
    }

    public void setSenderinfo(Senderinfo senderinfo) {
        this.senderinfo = senderinfo;
    }

}
