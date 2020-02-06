package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AndroidDev on 25-Oct-18.
 */

public class ProfileOrdination {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
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
    private UserData user_data;
    @SerializedName("ordination_level")
    @Expose
    private String ordination_level;
    @SerializedName("ordinated_name")
    @Expose
    private String ordinated_name;



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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

    public UserData getUser_data() {
        return user_data;
    }

    public void setUser_data(UserData user_data) {
        this.user_data = user_data;
    }

    public String getOrdination_level() {
        return ordination_level;
    }

    public void setOrdination_level(String ordination_level) {
        this.ordination_level = ordination_level;
    }

    public String getOrdinated_name() {
        return ordinated_name;
    }

    public void setOrdinated_name(String ordinated_name) {
        this.ordinated_name = ordinated_name;
    }
}
