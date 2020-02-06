package com.yogi_app.banbuser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.squareup.picasso.Picasso;
import com.yogi_app.R;
import com.yogi_app.activity.MainActivity;
import com.yogi_app.banbuser.modelClass.AddVideoMessageResponse;
import com.yogi_app.banbuser.modelClass.fetch_comments.GetFetchVideoMessageResponse;
import com.yogi_app.banbuser.modelClass.getChatModel.Chat;
import com.yogi_app.banbuser.modelClass.getChatModel.GetVideoMessageResponse;
import com.yogi_app.fcm.MyFirebaseMessagingService;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.TinyDB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecieverActivity extends AppCompatActivity {
    SurfaceView mVideoSurface;
    TextView mPlayerStatusTextView;

    private static final String APPLICATION_ID = "1NKozcHNqEJhHXijswoMag";
    private static final String API_KEY = "6761ep73ficm1onkfiljawg6m";
    final OkHttpClient mOkHttpClient = new OkHttpClient();
    BroadcastPlayer mBroadcastPlayer;
    MediaController mMediaController = null;

    @Bind(R.id.backbtn)
    ImageView backbtn;
    private RecyclerView.LayoutManager layoutManager;
    public GetChatListAdapter chatListAdapter;
    private RecyclerView chatRecyclerView;
    private TinyDB tinyDB;
    private EditText etChatBbox;
    private ImageView profile_img2, chatBtn;
    private List<com.yogi_app.banbuser.modelClass.fetch_comments.Chat> chatDataList;
    //private List<Chat> chatDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciever);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);
        mVideoSurface = (SurfaceView) findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView = (TextView) findViewById(R.id.PlayerStatusTextView);
        chatRecyclerView = findViewById(R.id.recycler_view_message);
        etChatBbox = findViewById(R.id.et_chat_box);
        profile_img2 = findViewById(R.id.profile_img2);

        chatBtn = findViewById(R.id.chat_btn);
        profile_img2.setImageResource(R.drawable.ic_emoji);


        if (MyFirebaseMessagingService.video_id_s != null) {
            tinyDB.putString("video_id_s", MyFirebaseMessagingService.video_id_s);
        }
        fetchVideoComments();
        // GetChatListmethod();
        BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
            @Override
            public void onStateChange(PlayerState playerState) {
                if (mPlayerStatusTextView != null)
                    mPlayerStatusTextView.setText("Status: " + playerState);
            }

            @Override
            public void onBroadcastLoaded(boolean live, int width, int height) {
            }
        };
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(RecieverActivity.this, "gfhfh", Toast.LENGTH_SHORT).show();
                //MainActivity.onMessageStatusbarIndication(false);
                startActivity(new Intent(RecieverActivity.this, MainActivity.class));


            }
        });
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecieverActivity.this, "msg sent", Toast.LENGTH_SHORT).show();
                closeKeyboard();
                ChatListmethod();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mOkHttpClient.dispatcher().cancelAll();
        mVideoSurface = null;

        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;

        if (mMediaController != null)
            mMediaController.hide();
        mMediaController = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoSurface = (SurfaceView) findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView.setText("Loading latest broadcast");
        if(MyFirebaseMessagingService.video_id_s!=null) {
            tinyDB.putString("video_id_s", MyFirebaseMessagingService.video_id_s);
        }
        getLatestResourceUri();
    }

    void getLatestResourceUri() {
        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPlayerStatusTextView != null)
                            mPlayerStatusTextView.setText("Http exception: " + e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                String body = response.body().string();
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    JSONObject latestBroadcast = results.optJSONObject(0);
                    resourceUri = latestBroadcast.optString("resourceUri");
                } catch (Exception ignored) {
                }
                final String uri = resourceUri;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPlayer(uri);
                    }
                });
            }
        });
    }

    void initPlayer(String resourceUri) {
        if (resourceUri == null) {
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("Could not get info about latest broadcast");
            return;
        }
        if (mVideoSurface == null) {
            // UI no longer active
            return;
        }
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();
    }

    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            // ...
            if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED || playerState == PlayerState.COMPLETED) {
                if (mMediaController == null && mBroadcastPlayer != null && !mBroadcastPlayer.isTypeLive()) {
                    mMediaController = new MediaController(RecieverActivity.this);
                    mMediaController.setAnchorView(mVideoSurface);
                    mMediaController.setMediaPlayer(mBroadcastPlayer);
                }
                if (mMediaController != null) {
                    mMediaController.setEnabled(true);
                    mMediaController.show();
                }
            } else if (playerState == PlayerState.ERROR || playerState == PlayerState.CLOSED) {
                if (mMediaController != null) {
                    mMediaController.setEnabled(false);
                    mMediaController.hide();
                }
                mMediaController = null;
            }
        }

        @Override
        public void onBroadcastLoaded(boolean b, int i, int i1) {

        }
        // ...
    };

    private void ChatListmethod() {
        System.out.println("GetChatListmethod" + "yhe gaya2 sent0..." + tinyDB.getString("video_id_s") + "..nm,..");
        System.out.println("GetChatListmethod" + "yhe gaya2 sent1.." + tinyDB.getInt("user_id"));
        if (MainApplication.isNetworkAvailable(RecieverActivity.this)) {
            // prog.progDialog(ac);
            //  if(!comment_send_image){
            ChatParametersModel request = new ChatParametersModel();
            // MyFirebaseMessagingService.video_id_s
            request.setVideo_id(tinyDB.getString("video_id_s"));
            request.setSender_id(String.valueOf(tinyDB.getInt("user_id")));
            request.setMessage(etChatBbox.getText().toString());

            MainApplication.getApiService().setVideoChatList("text/plain", request)
                    .enqueue(new retrofit2.Callback<AddVideoMessageResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<AddVideoMessageResponse> call, retrofit2.Response<AddVideoMessageResponse> response) {

                            if (response.isSuccessful()) {
                                AddVideoMessageResponse addVideoMessageResponse = response.body();
                                //  System.out.println("productsList_variantList_frag222ssss001" + response.body().getRespCode());
                                if (addVideoMessageResponse.getRespCode().matches("1014")) {
                                    etChatBbox.setText("");

//                                   chatListAdapter.notifyItemInserted(chatListAdapter.getItemCount() - 1);
                                    // chatRecyclerView.smoothScrollToPosition(chatListAdapter.getItemCount() - 1);
                                    // chatListAdapter.notifyDataSetChanged();
                                    // GetChatListmethod();
                                    fetchVideoComments();
                                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                                    //  snakeBaar.showSnackBar(ac, response.body().getMessage(), mainLayout);
                                } else {
                                    // snakeBaar.showSnackBar(ac, response.body().getMessage(), profileName2);
                                }
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<AddVideoMessageResponse> call, Throwable t) {
                            // prog.hideProg();
                            t.printStackTrace();
                        }
                    });


        } else {
            // prog.hideProg();
        }
    }

    private void GetChatListmethod() {


        System.out.println("GetChatListmethod" + "yhe gaya get0..." + tinyDB.getString("video_id_s") + "..nm,..");
//        System.out.println("GetChatListmethod" + "yhe gaya get1..."    +chatDataList);

        if (MainApplication.isNetworkAvailable(RecieverActivity.this)) {

            ChatParametersModel request = new ChatParametersModel();
            request.setVideo_id(tinyDB.getString("video_id_s"));


            MainApplication.getApiService().getVideoChatList("text/plain", request)
                    .enqueue(new retrofit2.Callback<GetVideoMessageResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<GetVideoMessageResponse> call, retrofit2.Response<GetVideoMessageResponse> response) {

                            // swipeRefreshLayout.setRefreshing(false);
                            if (response.isSuccessful()) {
                                GetVideoMessageResponse getChatComments = response.body();
                                if (getChatComments.getRespCode().matches("1018")) {
//                                    chatDataList = getChatComments.getChats();
//                                    System.out.println("GetChatListmethod" + "yhe gaya get1..." + chatDataList);
//                                    layoutManager = new LinearLayoutManager(RecieverActivity.this, LinearLayoutManager.VERTICAL, false);
//                                    chatListAdapter = new GetChatListAdapter(RecieverActivity.this, chatDataList);
//                                    layoutManager.scrollToPosition(chatListAdapter.getItemCount() - 1);
//                                    chatRecyclerView.setLayoutManager(layoutManager);
//                                    chatRecyclerView.setAdapter(chatListAdapter);
//

                                } else {
                                    // snakeBaar.showSnackBar(ac, response.body().getMessage(), getView());
                                }
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<GetVideoMessageResponse> call, Throwable t) {
                            // swipeRefreshLayout.setRefreshing(false);
                            t.printStackTrace();
                        }
                    });
        } else {
            // snakeBaar.showSnackBar(ac, "NO INTERNET CONNECTION", getView());
            //swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void fetchVideoComments() {

        if (MainApplication.isNetworkAvailable(RecieverActivity.this)) {

            ChatParametersModel request = new ChatParametersModel();
            request.setVideo_id(tinyDB.getString("video_id_s"));

            MainApplication.getApiService().getfetchVideoComments("text/plain", request)
                    .enqueue(new retrofit2.Callback<GetFetchVideoMessageResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<GetFetchVideoMessageResponse> call, retrofit2.Response<GetFetchVideoMessageResponse> response) {

                            // swipeRefreshLayout.setRefreshing(false);
                            if (response.isSuccessful()) {
                                GetFetchVideoMessageResponse getChatComments = response.body();
                                if (getChatComments.getRespCode().matches("1018")) {
                                    chatDataList = getChatComments.getChats();
                                    System.out.println("GetChatListmethod" + "yhe gaya getfetch1..." + chatDataList);
                                    layoutManager = new LinearLayoutManager(RecieverActivity.this, LinearLayoutManager.VERTICAL, false);
                                    chatListAdapter = new GetChatListAdapter(RecieverActivity.this, chatDataList);
                                    layoutManager.scrollToPosition(chatListAdapter.getItemCount() - 1);
                                    chatRecyclerView.setLayoutManager(layoutManager);
                                    chatRecyclerView.setAdapter(chatListAdapter);
                                    // chatListAdapter.notifyDataSetChanged();

                                } else {
                                    // snakeBaar.showSnackBar(ac, response.body().getMessage(), getView());
                                }
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<GetFetchVideoMessageResponse> call, Throwable t) {
                            // swipeRefreshLayout.setRefreshing(false);
                            t.printStackTrace();
                        }
                    });


        } else {
            // snakeBaar.showSnackBar(ac, "NO INTERNET CONNECTION", getView());
            //swipeRefreshLayout.setRefreshing(false);
        }


    }


    public class GetChatListAdapter extends RecyclerView.Adapter<GetChatListAdapter.MyViewHolder> {

        private List<com.yogi_app.banbuser.modelClass.fetch_comments.Chat> chatDataList2;
        private Context context;

        /*  public GetChatListAdapter(RecieverActivity recieverActivity, List<Chat> chatDataList) {
              this.context = recieverActivity;
              this.chatDataList2 = chatDataList;
          }
  */
        public GetChatListAdapter(RecieverActivity recieverActivity, List<com.yogi_app.banbuser.modelClass.fetch_comments.Chat> chatDataList) {

            this.context = recieverActivity;
            this.chatDataList2 = chatDataList;
        }

//        private List<Commentsarray> comentsList;
//        private Context context;


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_lit_design, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.sendImage.setVisibility(View.GONE);
            holder.profileName.setText(chatDataList2.get(position).getSenderinfo().getName());
            Picasso.with(context).load("" + chatDataList2.get(position).getSenderinfo().getImage()).into(holder.profileImage);
            // holder.linearLayoutChatInText.setVisibility(View.VISIBLE);
            holder.allComments.setText(chatDataList2.get(position).getMessages());
            holder.comentsDateText.setText(chatDataList2.get(position).getFormatDate());
            /*
            if (!comentsList.get(position).getSend_image().matches("")) {
                holder.sendImage.setVisibility(View.VISIBLE);
                try {
                    //  System.out.println("image_found.." + comentsList.get(position).getSend_image());
                    Picasso.with(ac).load("" + comentsList.get(position).getSend_image()).into(holder.sendImage);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }

            }
            holder.relLayout21.setOnTouchListener(new OnSwipeTouchListener(ac) {
                public void onSwipeRight() {
                    ac.overridePendingTransition(R.anim.left_side_anim,R.anim.right_side_anim);
                    ProfileFragment myf = new ProfileFragment();
                    showFragment(myf, "ProfileFragment");
                }

                public void onSwipeLeft() {
                    //  Toast.makeText(ac, "left1", Toast.LENGTH_SHORT).show();
                    ac.overridePendingTransition(R.anim.right_side_anim,R.anim.left_side_anim);
                    MyCartFragment myf = new MyCartFragment();
                    showFragment(myf, "MyCartFragment");
                }
            });
            holder.sendImage.setOnClickListener(v -> {
                if (!comentsList.get(position).getSend_image().matches("")) {

                    final Dialog dialog = new Dialog(ac, android.R.style.TextAppearance_Theme);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    // dialog.setCancelable(false);
                    dialog.setContentView(R.layout.img_preview);
                    dialog.show();

                    ImageView back_icon = (ImageView) dialog.findViewById(R.id.back_icon);

                    final ZoomageView img = (ZoomageView) dialog.findViewById(R.id.image_cool);

                    back_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });
                    System.out.println("bujyhvvvvvvvvvvvv" + comentsList.get(position).getSend_image());

                    try {
                        Picasso.with(ac).load("" + comentsList.get(position).getSend_image()).resize(500, 500)
                                .into(img);


                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("dddddddddddh" + e);
                    }
                }

            });*/

        }

        @Override
        public int getItemCount() {

            return chatDataList2.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView allComments, profileName, comentsDateText;
            private ImageView profileImage, sendImage;
            private LinearLayout linearLayoutChatInText ;
            private RelativeLayout relLayout21 /*linearLayoutChatImg*/;

            public MyViewHolder(View itemView) {
                super(itemView);
                profileImage = itemView.findViewById(R.id.profile_img_list);
                profileName = itemView.findViewById(R.id.profile_name_list);
                allComments = itemView.findViewById(R.id.tv_comments_list);
                comentsDateText = itemView.findViewById(R.id.tv_comments_date_list);

                linearLayoutChatInText = itemView.findViewById(R.id.linearLayout_chat_in_text);

                relLayout21 = itemView.findViewById(R.id.relLayout21);
                // linearLayoutChatImg = itemView.findViewById(R.id.linearLayout_chat_in_img);
                sendImage = itemView.findViewById(R.id.chat_in_img);
            }
        }
    }

    private void closeKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)RecieverActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RecieverActivity.this, MainActivity.class));
    }
}
