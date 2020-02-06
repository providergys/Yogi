
package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileUserInforResponse {

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
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ordination_level")
    @Expose
    private String ordination_level;
    @SerializedName("ordinated_name")
    @Expose
    private String ordinated_name;
    @SerializedName("ordinated_id")
    @Expose
    private String ordinated_id;



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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getOrdinated_id() {
        return ordinated_id;
    }

    public void setOrdinated_id(String ordinated_id) {
        this.ordinated_id = ordinated_id;
    }
}
