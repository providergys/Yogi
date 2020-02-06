
package com.yogi_app.banbuser.modelClass.getChatModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Senderinfo {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
