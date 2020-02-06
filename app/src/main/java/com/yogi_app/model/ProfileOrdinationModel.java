
package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileOrdinationModel {

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
    @SerializedName("user_data")
    @Expose
    private UserData userData;
    @SerializedName("ordinated_status")
    @Expose
    private Integer ordinatedStatus;
    @SerializedName("ordinated_id")
    @Expose
    private Integer ordinatedId;
    @SerializedName("ordinated_date")
    @Expose
    private String ordinatedDate;
    @SerializedName("ordinated_name")
    @Expose
    private String ordinatedName;

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

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Integer getOrdinatedStatus() {
        return ordinatedStatus;
    }

    public void setOrdinatedStatus(Integer ordinatedStatus) {
        this.ordinatedStatus = ordinatedStatus;
    }

    public Integer getOrdinatedId() {
        return ordinatedId;
    }

    public void setOrdinatedId(Integer ordinatedId) {
        this.ordinatedId = ordinatedId;
    }

    public String getOrdinatedDate() {
        return ordinatedDate;
    }

    public void setOrdinatedDate(String ordinatedDate) {
        this.ordinatedDate = ordinatedDate;
    }

    public String getOrdinatedName() {
        return ordinatedName;
    }

    public void setOrdinatedName(String ordinatedName) {
        this.ordinatedName = ordinatedName;
    }
}
