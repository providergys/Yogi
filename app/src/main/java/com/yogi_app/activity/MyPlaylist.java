package com.yogi_app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.yogi_app.R;
import com.yogi_app.adapters.MyPlaylistAdapter;
import com.yogi_app.fragments.AudioPlayList;
import com.yogi_app.model.DataModel;
import com.yogi_app.model.DrawerItemCustomAdapter;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.FetchPlaylistResponse;
import com.yogi_app.model.YogiPagerAdapter;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.ProgDialog;
import com.yogi_app.utility.SnakeBaar;
import com.yogi_app.utility.TinyDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPlaylist extends AppCompatActivity {

    ImageView back_icon, menu_icon;
    public static DrawerLayout mDrawerLayout;
    public static DrawerItemCustomAdapter drawer_adapter;
    ListView mDrawerList;
    Activity ac;
    Typeface mFont;
    TinyDB tinyDB;
    public static List<FetchPlaylistResponse> audioList = new ArrayList<>();
    public static List<FetchPlaylistResponse> songsList = new ArrayList<>();
    public static List<FetchPlaylistResponse> vedioList = new ArrayList<>();
    @Bind(R.id.navigation_drawer_text)
    TextView navigation_drawer_text;
    @Bind(R.id.gallery_header)
    TextView gallery_header;
    MyPlaylistAdapter adapter;
    ViewPager viewPager;
    SnakeBaar snakeBaar = new SnakeBaar();
    TabLayout tabs;
    RelativeLayout rel_layout;
    @Bind(R.id.drawer_profile_image)
    CircularImageView circularImageView;
    ProgDialog progDialog = new ProgDialog();
    ImageView imgSettings, imgSignOut;
    private String[] tabTitles;


    public MyPlaylist() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_playlist);


        ac = this;
        ButterKnife.bind(ac);
        tinyDB = new TinyDB(this);
        tabTitles = new String[]{getString(R.string.audioBooks),getString(R.string.songs),getString(R.string.videos)};
        imgSettings = (ImageView)findViewById(R.id.imgSettings);
        imgSignOut = (ImageView)findViewById(R.id.imgSignOut);


        if(tinyDB.getString("LOGIN").matches("user_login")){
            imgSettings.setVisibility(View.VISIBLE);
            imgSignOut.setVisibility(View.VISIBLE);
        }else{
            imgSettings.setVisibility(View.INVISIBLE);
            imgSignOut.setVisibility(View.INVISIBLE);
        }

        rel_layout=(RelativeLayout) findViewById(R.id.rel_layout);

        mFont = Typeface.createFromAsset(getAssets(), "robotlight.ttf");
        navigation_drawer_text.setTypeface(mFont);
        gallery_header.setTypeface(mFont);
        gallery_header.setText(R.string.myPlaylist);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.categoriesleft_drawer);
        back_icon = (ImageView) findViewById(R.id.back_icon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        menu_icon = (ImageView) findViewById(R.id.menu_icon);
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                if (position == 0) {
                    Intent gallery = new Intent(ac, MainActivity.class);
                    startActivity(gallery);
                } else if (position == 1) {
                    Intent gallery = new Intent(ac, GalleryActivity.class);
                    startActivity(gallery);
                } else if (position == 2) {
                    Intent gallery = new Intent(ac, YogiLibraryActivity.class);
                    startActivity(gallery);
                } else if (position == 3) {
                    if(tinyDB.getString("LOGIN").matches("user_login")){
                        Intent library = new Intent(ac, FaqActivity.class);
                        startActivity(library);
                    }else{
                        Toast.makeText(MyPlaylist.this,"Please Login...",Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 4) {
                    Intent calendar = new Intent(ac, Calender.class);
                    startActivity(calendar);
                } else if (position == 5) {
                    Intent contactUS = new Intent(ac, ContactUs.class);
                    startActivity(contactUS);
                } else if (position == 6) {
                    if(tinyDB.getString("LOGIN").matches("user_login")){
                        Intent playList = new Intent(ac, MyPlaylist.class);
                        startActivity(playList);
                    }else{
                        Toast.makeText(MyPlaylist.this,"Please Login...",Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 7) {
                    Intent engageUS = new Intent(ac, EngageUs.class);
                    startActivity(engageUS);
                } else if (position == 8) {
                    Intent qrScan = new Intent(ac, QrscanActivity.class);
                    startActivity(qrScan);
                } else if (position == 9) {

                }
            }
        });



       viewPager = (ViewPager) findViewById(R.id.viewpager);
         tabs = (TabLayout) findViewById(R.id.result_tabs);

        tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.audioBooks)));
        tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.songs)));
        tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.videos)));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);





        final DataModel[] drawerItem = new DataModel[9];

        drawerItem[0] = new DataModel(R.drawable.profile_icon, getResources().getString(R.string.home));
        drawerItem[1] = new DataModel(R.drawable.home_icon, getResources().getString(R.string.gallery));
        drawerItem[2] = new DataModel(R.drawable.gallery_icon, getResources().getString(R.string.library));
        drawerItem[3] = new DataModel(R.drawable.library_icon, getResources().getString(R.string.faqs));
        drawerItem[4] = new DataModel(R.drawable.faq_icon, getResources().getString(R.string.calendar));
        drawerItem[5] = new DataModel(R.drawable.calender_icon, getResources().getString(R.string.contactUs));
        drawerItem[6] = new DataModel(R.drawable.contact_icon, getResources().getString(R.string.myPlaylist));
        drawerItem[7] = new DataModel(R.drawable.play_list_icon, getResources().getString(R.string.engageUs));
        drawerItem[8] = new DataModel(R.drawable.engage_icon, getResources().getString(R.string.qrCode));
        //drawerItem[9] = new DataModel(R.drawable.scan_icon, "QR Code Scanner");
        drawer_adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(drawer_adapter);

        String p = tinyDB.getString("profile"); 
        String u = tinyDB.getString("userName");

//        uimg = tinyDB.getString("updated_image");
//        uUsername = tinyDB.getString("updated_email");
//        Log.e("UpdatedImage", uimg);
//        Toast.makeText(MyPlaylist.this,""+p +u,Toast.LENGTH_SHORT).show();
        if (p.isEmpty()) {
            circularImageView.setImageResource(R.drawable.guru_pic);
        } else {
            Picasso.with(MyPlaylist.this).load(p).error(R.drawable.place_holder).into(circularImageView);
        }

        navigation_drawer_text.setText(u);

        String integratedUserName = tinyDB.getString("userName");
        String profileImg = tinyDB.getString("profileImage");
        Log.e("INTIMG", profileImg);
        //Integration profile image and name in DrawerLayout
        if (integratedUserName != null) {
            navigation_drawer_text.setText(integratedUserName);
        }
        if (profileImg.isEmpty()) {
            circularImageView.setImageResource(R.drawable.guru_pic);
        } else {
            try {
                System.out.println("IMAeopgihyuseu7hobGE" + profileImg);
                Picasso.Builder builder = new Picasso.Builder(this);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                        System.out.println("IMAeopgihyuseu7hobGE" + exception);
                    }
                });
                builder.build().load(profileImg).into(circularImageView);
            } catch (Exception e) {
                Log.e("EXCEption", e.toString());
                e.printStackTrace();
            }
        }

//updating user profile image and name...
        try {
            String profile_img = tinyDB.getString("updated_image");
            String profileEmail = tinyDB.getString("updated_email");
            System.out.println("IMAeopgihyuseu7hobGE" + profile_img + profileEmail);

            if (profile_img.isEmpty()) {
                circularImageView.setImageResource(R.drawable.guru_pic);
            } else {
                try {
                    Picasso.Builder builder = new Picasso.Builder(this);
                    builder.listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            exception.printStackTrace();
                            System.out.println("IMAeopgihyuseu7hobGE" + exception);
                        }
                    });
                    builder.build().load(profile_img).error(R.drawable.place_holder).into(circularImageView);
                } catch (Exception e) {
                    Log.e("EXCEption", e.toString());
                    e.printStackTrace();
                }
                // Picasso.with(MainActivity.this).load(profileImg).into(circularImageView);
            }
        } catch (Exception e) {
            Log.e("EXCEption", e.toString());
            e.printStackTrace();
        }
        apiFetchPlayList();


    }
    @OnClick(R.id.imgSettings)
    void settings(){
        Intent profile = new Intent(ac, Profile.class);
        startActivity(profile);
    }

    @OnClick(R.id.imgSignOut)
    void signOut(){
        tinyDB.clear();
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }
    public void apiFetchPlayList(){
        progDialog.progDialog(MyPlaylist.this);
        FaqResponse request = new FaqResponse();
        Integer userId = tinyDB.getInt("user_id");
        request.setUserId(userId);

        MainApplication.getApiService().fetchPlaylist("text/plain", request).enqueue(new Callback<FetchPlaylistResponse>() {
            @Override
            public void onResponse(Call<FetchPlaylistResponse> call, Response<FetchPlaylistResponse> response) {

                FetchPlaylistResponse playlistResponse = response.body();
                if (response.body().getRespCode().matches("2027")) {

                    audioList = playlistResponse.getUpdated_palylist().getAudioBookList();
                    songsList = playlistResponse.getUpdated_palylist().getAudiooList();
                    vedioList = playlistResponse.getUpdated_palylist().getVideoList();
                    adapter = new MyPlaylistAdapter(getSupportFragmentManager(),tabTitles);
                    viewPager.setAdapter(adapter);
                    tabs.setupWithViewPager(viewPager);
                    progDialog.hideProg();
                 //   Log.d("LIST",""+playlistResponse.getUpdated_palylist().getAudioBookList().size());
                  //  Toast.makeText(MyPlaylist.this, "" + playlistResponse.getUpdated_palylist().getAudioBookList().size(), Toast.LENGTH_SHORT).show();
//
                } else {

                 //   Toast.makeText(MyPlaylist.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    snakeBaar.showSnackBar(MyPlaylist.this,""+response.body().getMessage(),rel_layout);

                }
            }

            @Override
            public void onFailure(Call<FetchPlaylistResponse> call, Throwable t) {


            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent newIntent = new Intent(ac,MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        super.onBackPressed();
    }
}
