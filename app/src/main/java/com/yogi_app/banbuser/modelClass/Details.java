
package com.yogi_app.banbuser.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("sender_info")
    @Expose
    private SenderInfo senderInfo;
    @SerializedName("messages")
    @Expose
    private String messages;
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("video_id")
    @Expose
    private String videoId;
    @SerializedName("format_date")
    @Expose
    private String formatDate;
    @SerializedName("format_time")
    @Expose
    private String formatTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public SenderInfo getSenderInfo() {
        return senderInfo;
    }

    public void setSenderInfo(SenderInfo senderInfo) {
        this.senderInfo = senderInfo;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

}
