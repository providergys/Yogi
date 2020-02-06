
package com.yogi_app.alipay;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdinationLevels {

    @SerializedName("user_ordination_info")
    @Expose
    private List<UserOrdinationInfo> userOrdinationInfo = new ArrayList<>();

    public List<UserOrdinationInfo> getUserOrdinationInfo() {
        return userOrdinationInfo;
    }

    public void setUserOrdinationInfo(List<UserOrdinationInfo> userOrdinationInfo) {
        this.userOrdinationInfo = userOrdinationInfo;
    }

}
