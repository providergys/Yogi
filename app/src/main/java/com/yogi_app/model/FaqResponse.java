
package com.yogi_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FaqResponse  {


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
    @SerializedName("FAQ")
    @Expose
    List<FaqResponse> list = new ArrayList<>();
    @SerializedName("ques")
    @Expose
    private String question;
    @SerializedName("ans")
    @Expose
    private String answer;
    @SerializedName("contact_array")
    @Expose
    List<FaqResponse> listContacts = new ArrayList<>();
    @SerializedName("contact_name")
    @Expose
    private String contactName;
    @SerializedName("contact_email")
    @Expose
    private String contactEmail;
    @SerializedName("contact_phone")
    @Expose
    private String contactPhone;
    @SerializedName("contact_image")
    @Expose
    private String contact_image;
    @SerializedName("videos_array")
    @Expose
    private List<FaqResponse> video  = new ArrayList<>();;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("dharma_books_array")
    @Expose
    List<FaqResponse> dharmaBooksList = new ArrayList<>();
    @SerializedName("audio_books_array")
    @Expose
    List<FaqResponse> audioBooksList = new ArrayList<>();
    @SerializedName("songs_array")
    @Expose
    List<FaqResponse> songsList = new ArrayList<>();
    @SerializedName("categories")
    @Expose
    List<FaqResponse> listCategories = new ArrayList<>();
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("items_array")
    @Expose
    List<FaqResponse> item_array_List = new ArrayList<>();
    @SerializedName("attachment_title")
    @Expose
    private String attachmentTitle;
    @SerializedName("attachment_image")
    @Expose
    private String attachment_image;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("attachment_url")
    @Expose
    private String attachmentUrl;
    @SerializedName("attachment_type")
    @Expose
    private String attachmentType;
    @SerializedName("length")
    @Expose
    private String length;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("media_id")
    @Expose
    private String mediaID;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("user_data")
    @Expose
    private UserData user_data;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("ordination_level")
    @Expose
    private String ordination_level;
    @SerializedName("ordinated_name")
    @Expose
    private String ordinated_name;



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

    public List<FaqResponse> getList() {
        return list;
    }

    public void setList(List<FaqResponse> list) {
        this.list = list;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<FaqResponse> getListContacts() {
        return listContacts;
    }

    public void setListContacts(List<FaqResponse> listContacts) {
        this.listContacts = listContacts;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContact_image() {
        return contact_image;
    }

    public void setContact_image(String contact_image) {
        this.contact_image = contact_image;
    }

    public List<FaqResponse> getVideoList() {
        return video;
    }

    public void setVideoList(List<FaqResponse> video) {
        this.video = video;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FaqResponse> getDharmaBooksList() {
        return dharmaBooksList;
    }

    public void setDharmaBooksList(List<FaqResponse> dharmaBooksList) {
        this.dharmaBooksList = dharmaBooksList;
    }

    public List<FaqResponse> getAudioBooksList() {
        return audioBooksList;
    }

    public void setAudioBooksList(List<FaqResponse> audioBooksList) {
        this.audioBooksList = audioBooksList;
    }

    public List<FaqResponse> getSongsList() {
        return songsList;
    }

    public void setSongsList(List<FaqResponse> songsList) {
        this.songsList = songsList;
    }

    public List<FaqResponse> getListCategories() {
        return listCategories;
    }

    public void setListCategories(List<FaqResponse> listCategories) {
        this.listCategories = listCategories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FaqResponse> getItem_array_List() {
        return item_array_List;
    }

    public void setItem_array_List(List<FaqResponse> item_array_List) {
        this.item_array_List = item_array_List;
    }

    public String getAttachmentTitle() {
        return attachmentTitle;
    }

    public void setAttachmentTitle(String attachmentTitle) {
        this.attachmentTitle = attachmentTitle;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getAttachment_image() {
        return attachment_image;
    }

    public void setAttachment_image(String attachment_image) {
        this.attachment_image = attachment_image;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UserData getUser_data() {
        return user_data;
    }

    public void setUser_data(UserData user_data) {
        this.user_data = user_data;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
