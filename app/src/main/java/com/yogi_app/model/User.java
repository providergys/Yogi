
package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yogi_app.alipay.UserOrdinationInfo;

import java.util.ArrayList;
import java.util.List;

public class User {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("selected_events_country")
    @Expose
    private List<String> eventsCountryList;
    @SerializedName("user_ordination_info")
    @Expose
    private List<UserOrdinationInfo> user_ordination_info = new ArrayList<>();
    @SerializedName("ordinated_status")
    @Expose
    private int ordinated_status;
    @SerializedName("country_residence")
    @Expose
    private String country_residence;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<String> getEventsCountryList() {
        return eventsCountryList;
    }

    public void setEventsCountryList(List<String> eventsCountryList) {
        this.eventsCountryList = eventsCountryList;
    }

    public List<UserOrdinationInfo> getUser_ordination_info() {
        return user_ordination_info;
    }

    public void setUser_ordination_info(List<UserOrdinationInfo> user_ordination_info) {
        this.user_ordination_info = user_ordination_info;
    }

    public int getOrdinated_status() {
        return ordinated_status;
    }

    public void setOrdinated_status(int ordinated_status) {
        this.ordinated_status = ordinated_status;
    }

    public String getCountry_residence() {
        return country_residence;
    }

    public void setCountry_residence(String country_residence) {
        this.country_residence = country_residence;
    }
}
