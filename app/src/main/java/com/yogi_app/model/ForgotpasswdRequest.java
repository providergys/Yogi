
package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotpasswdRequest {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("contact_num")
    @Expose
    private String contactNum;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }


}
