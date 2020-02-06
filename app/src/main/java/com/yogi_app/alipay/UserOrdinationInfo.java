
package com.yogi_app.alipay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOrdinationInfo {

    @SerializedName("ordinated_status")
    @Expose
    private Integer ordinatedStatus;
    @SerializedName("ordinated_id")
    @Expose
    private Integer ordinatedId;
    @SerializedName("ordinated_name")
    @Expose
    private String ordinatedName;
    @SerializedName("ordinated_date")
    @Expose
    private String ordinatedDate;

    public UserOrdinationInfo(Integer ordinatedStatus, Integer ordinatedId, String ordinatedName, String ordinatedDate) {
        this.ordinatedStatus = ordinatedStatus;
        this.ordinatedId = ordinatedId;
        this.ordinatedName = ordinatedName;
        this.ordinatedDate = ordinatedDate;
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

    public String getOrdinatedName() {
        return ordinatedName;
    }

    public void setOrdinatedName(String ordinatedName) {
        this.ordinatedName = ordinatedName;
    }

    public String getOrdinatedDate() {
        return ordinatedDate;
    }

    public void setOrdinatedDate(String ordinatedDate) {
        this.ordinatedDate = ordinatedDate;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
