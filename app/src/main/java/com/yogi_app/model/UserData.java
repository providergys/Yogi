
package com.yogi_app.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("profile_image")
    @Expose

    private String profileImage;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("dharmaname")
    @Expose
    private String dharmaname;
    @SerializedName("user_ordination_info")
    @Expose
    private Boolean userOrdinationInfo;
    @SerializedName("ordination_level")
    @Expose
    private List<ProfileUserInforResponse> ordinationLevel = new ArrayList<>();
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("country_residence")
    @Expose
    private String countryResidence;
    @SerializedName("userrole")
    @Expose
    private String user_role;

    @SerializedName("All_event_locations")
    @Expose
    private List<ProfileUserInforResponse> allEventLocations = new ArrayList<>();
    @SerializedName("selected_events_country")
    @Expose
    private List<String> selectedEventsCountry = null;
    @SerializedName("ordination_array")
    @Expose
    private List<OrdinationArray> ordinationArray = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    public String getDharmaname() {
        return dharmaname;
    }

    public void setDharmaname(String dharmaname) {
        this.dharmaname = dharmaname;
    }

    public Boolean getUserOrdinationInfo() {
        return userOrdinationInfo;
    }

    public void setUserOrdinationInfo(Boolean userOrdinationInfo) {
        this.userOrdinationInfo = userOrdinationInfo;
    }

    public List<ProfileUserInforResponse> getOrdinationLevel() {
        return ordinationLevel;
    }

    public void setOrdinationLevel(List<ProfileUserInforResponse> ordinationLevel) {
        this.ordinationLevel = ordinationLevel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountryResidence() {
        return countryResidence;
    }

    public void setCountryResidence(String countryResidence) {
        this.countryResidence = countryResidence;
    }

    public List<ProfileUserInforResponse> getAllEventLocations() {
        return allEventLocations;
    }

    public void setAllEventLocations(List<ProfileUserInforResponse> allEventLocations) {
        this.allEventLocations = allEventLocations;
    }

    public List<String> getSelectedEventsCountry() {
        return selectedEventsCountry;
    }

    public void setSelectedEventsCountry(List<String> selectedEventsCountry) {
        this.selectedEventsCountry = selectedEventsCountry;
    }

    public List<OrdinationArray> getOrdinationArray() {
        return ordinationArray;
    }

    public void setOrdinationArray(List<OrdinationArray> ordinationArray) {
        this.ordinationArray = ordinationArray;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }
}
