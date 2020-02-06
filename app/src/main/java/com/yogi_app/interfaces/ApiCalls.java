package com.yogi_app.interfaces;


import com.yogi_app.activity.Login;
import com.yogi_app.banbuser.ChatParametersModel;
import com.yogi_app.banbuser.modelClass.AddVideoMessageResponse;
import com.yogi_app.banbuser.modelClass.fetch_comments.GetFetchVideoMessageResponse;
import com.yogi_app.banbuser.modelClass.getChatModel.GetVideoMessageResponse;
import com.yogi_app.model.CalendarRequest;
import com.yogi_app.model.CalendarResponse;
import com.yogi_app.model.FaqRequest;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.FetchPlaylistResponse;
import com.yogi_app.model.ForgotpasswdRequest;
import com.yogi_app.model.ForgotpasswdResponse;
import com.yogi_app.model.IntroResponse;
import com.yogi_app.model.LoginRequest;
import com.yogi_app.model.LoginResponse;
import com.yogi_app.model.ProfileOrdination;
import com.yogi_app.model.ProfileOrdinationModel;
import com.yogi_app.model.ProfileUserInforResponse;
import com.yogi_app.model.QuizModel;
import com.yogi_app.model.SignUpRequest;
import com.yogi_app.model.SignUpResponse;
import com.yogi_app.model.SocialLoginRequest;
import com.yogi_app.model.SocialLoginResponse;
import com.yogi_app.model.UpdateplaylistRequest;
import com.yogi_app.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Aman kashyap on 05-10-208.
 */


public interface  ApiCalls {


    @POST("login")
    Call<LoginResponse> login_method(@Header("Content-Type") String content_type,
                                     @Body LoginRequest loginRequest);

    @POST("forgotpassword")
    Call<ForgotpasswdResponse> forgot_method(@Header("Content-Type") String content_type,
                                             @Body ForgotpasswdRequest forgotpasswdRequest);

    @POST("signup")
    Call<SignUpResponse> signup(@Header("Content-Type") String content_type,
                                @Body SignUpRequest signUpRequest);

    @GET("get_intro")
    Call<IntroResponse> aboutusRequestmethod(@Header("Content-Type") String content_type);

    @POST("add_faq")
    Call<FaqResponse> faq_method(@Header("Content-Type") String content_type,
                                 @Body FaqRequest faqRequest);

    @GET("fetch_faq")
    Call<FaqResponse> fetchFAQS(@Header("Content-Type") String content_type);

    @GET("contact_us")
    Call<FaqResponse> ContactUs(@Header("Content-Type") String content_type);

    @GET("fetch_videos")
    Call<FaqResponse> fetchVideos(@Header("Content-Type") String content_type);

    @GET("fetch_dharma_books")
    Call<FaqResponse> fetchDharmaBooks(@Header("Content-Type") String content_type);

    @GET("fetch_audio_books")
    Call<FaqResponse> fetchAudioBooks(@Header("Content-Type") String content_type);

    @GET("fetch_songs")
    Call<FaqResponse> fetchSongs(@Header("Content-Type") String content_type);

    @GET("list_gallery_category")
    Call<FaqResponse> fetchCategories(@Header("Content-Type") String content_type);

    @POST("fetch_category_files")
    Call<FaqResponse> fetchCategoriesListItems(@Header("Content-Type") String content_type,
                                 @Body FaqResponse faqRequest);

    @GET("fetch_engage_us_posts")
    Call<FaqResponse> fetchEngageUS(@Header("Content-Type") String content_type);

    @POST("update_playlist")
    Call<FaqResponse> sendSelectedMediaFiles(@Header("Content-Type") String content_type,
                                               @Body FaqResponse request);

    @POST("fetch_playlist")
    Call<FetchPlaylistResponse> fetchPlaylist(@Header("Content-Type") String content_type,
                                              @Body FaqResponse request);
    @POST("getProfile")
    Call<ProfileUserInforResponse> fetchProfileData(@Header("Content-Type") String content_type, @Body FaqResponse request);

    @POST("updateProfile")
    Call<FaqResponse> updateProfileData(@Header("Content-Type") String content_type,@Body User request);

    @POST("change_password")
    Call<LoginResponse> changePassword(@Header("Content-Type") String content_type,@Body FaqResponse request);

    @POST("social_login")
    Call<SocialLoginResponse> social_login(@Header("Content-Type") String content_type, @Body SocialLoginRequest socialLoginRequest);

    @POST("Add_event")
    Call<CalendarResponse> addCalendarEvent(@Header("Content-Type") String content_type, @Body CalendarRequest request);

    @POST("Get_Event")
    Call<CalendarResponse> getCalendarEvents(@Header("Content-Type") String content_type,@Body CalendarRequest request);

    @POST("add_favourite")
    Call<CalendarResponse> addFavouriteEvent(@Header("Content-Type") String content_type, @Body CalendarRequest request);

    @GET("quiz_level")
    Call<QuizModel> getQuizLevel(@Header("Content-Type") String content_type);

    @POST("get_quiz")
    Call<QuizModel> getQuizQuestions(@Header("Content-Type") String content_type, @Body QuizModel request);

    @POST("quiz_answer_submit")
    Call<QuizModel> submitQuizAnswers(@Header("Content-Type") String content_type, @Body QuizModel request);

    @GET("updateProfile")
    Call<User> getEventsCountryList(@Header("Content-Type") String content_type);

    @POST("add_video_comment")
    Call<AddVideoMessageResponse> setVideoChatList(@Header("Content-Type") String content_type, @Body ChatParametersModel request);

    @POST("get_latest_comments")
    Call<GetVideoMessageResponse> getVideoChatList(@Header("Content-Type") String content_type, @Body ChatParametersModel request);

    @POST("fetch_video_comments")
    Call<GetFetchVideoMessageResponse> getfetchVideoComments(@Header("Content-Type") String content_type, @Body ChatParametersModel request);

}