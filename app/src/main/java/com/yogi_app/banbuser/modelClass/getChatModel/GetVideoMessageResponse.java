
package com.yogi_app.banbuser.modelClass.getChatModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetVideoMessageResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("RespCode")
    @Expose
    private String respCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("chats")
    @Expose
    private List<Chat> chats = null;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

}
