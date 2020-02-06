
package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdinationLevel {

    @SerializedName("ordinated_id")
    @Expose
    private Integer ordinatedId;
    @SerializedName("ordinated_name")
    @Expose
    private String ordinatedName;

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

}
