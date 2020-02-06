
package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Social_Data {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}
