
package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("devicetoken")
    @Expose
    private String devicetoken;
    @SerializedName("devicetype")
    @Expose
    private String devicetype;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }
}
