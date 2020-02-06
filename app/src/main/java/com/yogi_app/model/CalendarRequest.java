package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalendarRequest {

    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("event_title")
    @Expose
    private String event_title;
    @SerializedName("about_event")
    @Expose
    private String about_event;
    @SerializedName("event_date")
    @Expose
    private String event_date;
    @SerializedName("event_time")
    @Expose
    private String event_time;
    @SerializedName("favourite_or_not")
    @Expose
    private String favouriteOrNot;
    @SerializedName("event_id")
    @Expose
    private String event_id;



    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getAbout_event() {
        return about_event;
    }

    public void setAbout_event(String about_event) {
        this.about_event = about_event;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getFavouriteOrNot() {
        return favouriteOrNot;
    }

    public void setFavouriteOrNot(String favouriteOrNot) {
        this.favouriteOrNot = favouriteOrNot;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }
}
