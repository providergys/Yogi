package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
//
//public class CalendarResponse {
//
//    @SerializedName("status")
//    @Expose
//    private String status;
//    @SerializedName("RespCode")
//    @Expose
//    private String respCode;
//    @SerializedName("success")
//    @Expose
//    private String success;
//    @SerializedName("Message")
//    @Expose
//    private String message;
//    @SerializedName("user_event_Data")
//    @Expose
//    private List<CalendarResponse> userEventData = new ArrayList<>();
//    @SerializedName("event_id")
//    @Expose
//    private String event_id;
//    @SerializedName("event_title")
//    @Expose
//    private String event_title;
//    @SerializedName("event_desc")
//    @Expose
//    private String about_event;
//    @SerializedName("event_date")
//    @Expose
//    private String event_date;
//    @SerializedName("event_time")
//    @Expose
//    private String event_time;
//    @SerializedName("favourite_or_not")
//    @Expose
//    private String favourite_or_not;
//    @SerializedName("event_location")
//    @Expose
//    private String event_location;
//
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getRespCode() {
//        return respCode;
//    }
//
//    public void setRespCode(String respCode) {
//        this.respCode = respCode;
//    }
//
//    public String getSuccess() {
//        return success;
//    }
//
//    public void setSuccess(String success) {
//        this.success = success;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public List<CalendarResponse> getUserEventData() {
//        return userEventData;
//    }
//
//    public void setUserEventData(List<CalendarResponse> userEventData) {
//        this.userEventData = userEventData;
//    }
//
//
//    public String getEvent_id() {
//        return event_id;
//    }
//
//    public void setEvent_id(String event_id) {
//        this.event_id = event_id;
//    }
//
//    public String getEvent_title() {
//        return event_title;
//    }
//
//    public void setEvent_title(String event_title) {
//        this.event_title = event_title;
//    }
//
//    public String getAbout_event() {
//        return about_event;
//    }
//
//    public void setAbout_event(String about_event) {
//        this.about_event = about_event;
//    }
//
//    public String getEvent_date() {
//        return event_date;
//    }
//
//    public void setEvent_date(String event_date) {
//        this.event_date = event_date;
//    }
//
//    public String getEvent_time() {
//        return event_time;
//    }
//
//    public void setEvent_time(String event_time) {
//        this.event_time = event_time;
//    }
//
//    public String getFavourite_or_not() {
//        return favourite_or_not;
//    }
//
//    public void setFavourite_or_not(String favourite_or_not) {
//        this.favourite_or_not = favourite_or_not;
//    }
//
//    public String getEvent_location() {
//        return event_location;
//    }
//
//    public void setEvent_location(String event_location) {
//        this.event_location = event_location;
//    }
//}

public class CalendarResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("RespCode")
    @Expose
    private String respCode;
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("user_event_Data")
    @Expose
    private List<CalendarResponse> userEventData = null;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("event_time")
    @Expose
    private String eventTime;
    @SerializedName("event_title")
    @Expose
    private String eventTitle;
    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("favourite_or_not")
    @Expose
    private String favouriteOrNot;
    @SerializedName("event_desc")
    @Expose
    private String eventDesc;
    @SerializedName("event_location")
    @Expose
    private List<EventLocation> eventLocation = null;
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("location_name")
    @Expose
    private String locationName;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CalendarResponse> getUserEventData() {
        return userEventData;
    }

    public void setUserEventData(List<CalendarResponse> userEventData) {
        this.userEventData = userEventData;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getFavouriteOrNot() {
        return favouriteOrNot;
    }

    public void setFavouriteOrNot(String favouriteOrNot) {
        this.favouriteOrNot = favouriteOrNot;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public List<EventLocation> getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(List<EventLocation> eventLocation) {
        this.eventLocation = eventLocation;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
