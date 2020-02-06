
package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLoginRequest {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("socialid")
    @Expose
    private String socialid;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("username")
    @Expose
    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocialid() {
        return socialid;
    }

    public void setSocialid(String socialid) {
        this.socialid = socialid;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
