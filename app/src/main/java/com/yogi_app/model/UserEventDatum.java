
package com.yogi_app.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserEventDatum {

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
    private Integer favouriteOrNot;
    @SerializedName("event_desc")
    @Expose
    private String eventDesc;
    @SerializedName("event_location")
    @Expose
    private List<EventLocation> eventLocation = null;

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

    public Integer getFavouriteOrNot() {
        return favouriteOrNot;
    }

    public void setFavouriteOrNot(Integer favouriteOrNot) {
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

}
