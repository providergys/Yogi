package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchPlaylistResponse {

    @SerializedName("RespCode")
    @Expose
    private String respCode;
    @SerializedName("updated_palylist")
    @Expose
    private FetchPlaylistResponse updated_palylist;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("length")
    @Expose
    private String length;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("audio_books")
    @Expose
    private List<FetchPlaylistResponse> audioBookList;
    @SerializedName("audio")
    @Expose
    private List<FetchPlaylistResponse> audiooList;
    @SerializedName("videos")
    @Expose
    private List<FetchPlaylistResponse> videoList;


    public FetchPlaylistResponse getUpdated_palylist() {
        return updated_palylist;
    }

    public void setUpdated_palylist(FetchPlaylistResponse updated_palylist) {
        this.updated_palylist = updated_palylist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
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

    public List<FetchPlaylistResponse> getAudioBookList() {
        return audioBookList;
    }

    public void setAudioBookList(List<FetchPlaylistResponse> audioBookList) {
        this.audioBookList = audioBookList;
    }

    public List<FetchPlaylistResponse> getAudiooList() {
        return audiooList;
    }

    public void setAudiooList(List<FetchPlaylistResponse> audiooList) {
        this.audiooList = audiooList;
    }

    public List<FetchPlaylistResponse> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<FetchPlaylistResponse> videoList) {
        this.videoList = videoList;
    }
}

