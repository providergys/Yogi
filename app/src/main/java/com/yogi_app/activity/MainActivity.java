package com.yogi_app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.yogi_app.R;
import com.yogi_app.banbuser.CameraBroadCastActivity;
import com.yogi_app.banbuser.RecieverActivity;
import com.yogi_app.model.DataModel;
import com.yogi_app.model.DrawerItemCustomAdapter;
import com.yogi_app.model.IntroResponse;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.ProgDialog;
import com.yogi_app.utility.TinyDB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    public static DrawerLayout mDrawerLayout;
    public static DrawerItemCustomAdapter adapter;
    ListView mDrawerList;
    ImageView menu_icon, back_btn;
    Activity ac;
    Typeface mFont;
    @Bind(R.id.tvIntro)
    TextView tvIntro;
    @Bind(R.id.tvSubTitle)
    TextView tvSubTitle;

    /* @Bind(R.id.tvTitle)*/
    static TextView tvTitle, tvSubTitle2;
    /*@Bind(R.id.redbar_tv)*/
    static TextView redbarTv;
    @Bind(R.id.navigation_drawer_text)
    TextView navigation_drawer_text;
    @Bind(R.id.drawer_profile_image)
    CircularImageView circularImageView;
    TinyDB tinyDB;
    String uimg, uUsername;
    ProgDialog prog = new ProgDialog();
    ImageView imgSettings, imgSignOut;
    TinyDB tiny_db;
    String langval = "", type_userAdmin="";
    private static boolean statusBarHit = false;
    private int arraySize = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ac = this;
        tiny_db = new TinyDB(ac);
        ButterKnife.bind(ac);
        redbarTv = findViewById(R.id.redbar_tv);
        tvTitle = findViewById(R.id.tvTitle);

        tinyDB = new TinyDB(MainActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        imgSettings = (ImageView) findViewById(R.id.imgSettings);
        imgSignOut = (ImageView) findViewById(R.id.imgSignOut);
        tvSubTitle2 =  findViewById(R.id.tvSubTitle2);

        //  tvTitle = findViewById(R.id.tvTitle);

        tvSubTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                statusBarHit =true;
                if(statusBarHit)
                {
                    try{
                        //redbarTv.setVisibility(View.VISIBLE);
                        redbarTv.setBackgroundColor(getResources().getColor(R.color.redStatusBar));
                        redbarTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_broadcast_black_24dp, 0);

                    }catch (Exception exc)
                    {
                        exc.printStackTrace();
                    }
                    try{
                        //redbarTv.setVisibility(View.VISIBLE);
                        // redbarTv.setBackgroundColor(getResources().getColor(R.color.redStatusBar));
                        redbarTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }catch (Exception exc)
                    {
                        exc.printStackTrace();
                    }

                    // redbarTv.setVisibility(View.VISIBLE);
                }else {
                    //redbarTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }

            }
        });

        tvSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, CameraBroadCastActivity.class));
            }
        });
        redbarTv.setOnClickListener(new View.OnClickListener() {
            //@SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
//                 Toast.makeText(ac, "Wait video will appear soon", Toast.LENGTH_SHORT).show();

                if(statusBarHit){
                    startActivity(new Intent(MainActivity.this, RecieverActivity.class));
                }
                statusBarHit =false;

            }
        });

        if (tinyDB.getString("LOGIN").matches("user_login")) {
            imgSettings.setVisibility(View.VISIBLE);
            // imgSignOut.setVisibility(View.VISIBLE);
        } else {
            imgSettings.setVisibility(View.INVISIBLE);
            imgSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
            });
//            imgSignOut.setVisibility(View.INVISIBLE);
        }
        mFont = Typeface.createFromAsset(getAssets(), "robotlight.ttf");

        tvIntro.setTypeface(mFont);
        tvSubTitle.setTypeface(mFont);
        tvTitle.setTypeface(mFont);
        navigation_drawer_text.setTypeface(mFont);
        //Getting hash key for facebook
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                System.out.println("fdsdsdsdsdsdsdsdsdsdsdsdsds" + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("fdsdsdsdsdsdsdsdsdsdsdsdsds" + e);
        } catch (Exception e) {
            System.out.println("fdsdsdsdsdsdsdsdsdsdsdsdsds" + e);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.categoriesleft_drawer);
        menu_icon = (ImageView) findViewById(R.id.menu_icon);
        back_btn = (ImageView) findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        if (tiny_db.getString("userrole").matches("administrator")) {
            // drawerItem[9] = new DataModel(R.drawable.ic_broadcast_black_24dp, getResources().getString(R.string.broadcasting));
            arraySize = 10;
        } else {
            arraySize = 10;
            // drawerItem[9] = new DataModel(R.drawable.list_bg_rounded, "");
        }

        final DataModel[] drawerItem = new DataModel[arraySize];

        drawerItem[0] = new DataModel(R.drawable.profile_icon, getResources().getString(R.string.home));
        drawerItem[1] = new DataModel(R.drawable.home_icon, getResources().getString(R.string.gallery));
        drawerItem[2] = new DataModel(R.drawable.gallery_icon, getResources().getString(R.string.library));
        drawerItem[3] = new DataModel(R.drawable.library_icon, getResources().getString(R.string.faqs));
        drawerItem[4] = new DataModel(R.drawable.faq_icon, getResources().getString(R.string.calendar));
        drawerItem[5] = new DataModel(R.drawable.calender_icon, getResources().getString(R.string.contactUs));
        drawerItem[6] = new DataModel(R.drawable.contact_icon, getResources().getString(R.string.myPlaylist));
        drawerItem[7] = new DataModel(R.drawable.play_list_icon, getResources().getString(R.string.engageUs));
        drawerItem[8] = new DataModel(R.drawable.engage_icon, getResources().getString(R.string.qrCode));

        if (tiny_db.getString("userrole").matches("administrator")) {
            type_userAdmin = "admin";
            drawerItem[9] = new DataModel(R.drawable.ic_broadcast_black_24dp, getResources().getString(R.string.broadcasting));
        } else {
            type_userAdmin = "user";
            drawerItem[9] = new DataModel(R.drawable.ic_broadcast_black_24dp, getResources().getString(R.string.reciever_act));
        }

        adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }

                if (position == 0) {
//                    Intent gallery = new Intent(ac, MainActivity.class);
//                    startActivity(gallery);
                } else if (position == 1) {
                    Intent gallery = new Intent(ac, GalleryActivity.class);
                    startActivity(gallery);
                } else if (position == 2) {
                    Intent gallery = new Intent(ac, YogiLibraryActivity.class);
                    startActivity(gallery);
                } else if (position == 3) {
                    if (tinyDB.getString("LOGIN").matches("user_login")) {
                        Intent library = new Intent(ac, FaqActivity.class);
                        startActivity(library);
                    } else {
                        Toast.makeText(MainActivity.this, "Please Login...", Toast.LENGTH_SHORT).show();
                    }

                } else if (position == 4) {
                    Intent calendar = new Intent(ac, Calender.class);
                    startActivity(calendar);
                } else if (position == 5) {
                    Intent contactUS = new Intent(ac, ContactUs.class);
                    startActivity(contactUS);
                } else if (position == 6) {

                    if (tinyDB.getString("LOGIN").matches("user_login")) {
                        Intent playList = new Intent(ac, MyPlaylist.class);
                        startActivity(playList);
                    } else {
                        Toast.makeText(MainActivity.this, "Please Login...", Toast.LENGTH_SHORT).show();
                    }

                } else if (position == 7) {
                    Intent engageUS = new Intent(ac, EngageUs.class);
                    startActivity(engageUS);
                } else if (position == 8) {
                    Intent qrScan = new Intent(ac, QrscanActivity.class);
                    startActivity(qrScan);
                } else if (position == 9) {

                    if(type_userAdmin.matches("admin")){
                        // type_userAdmin = "user";
                        Intent qrScan = new Intent(ac, CameraBroadCastActivity.class);
                        startActivity(qrScan);
                    }else {
                        Intent qrScan = new Intent(ac, RecieverActivity.class);
                        startActivity(qrScan);
                    }

                }
            }
        });

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

        try {
            String p = tinyDB.getString("profile");
            String u = tinyDB.getString("userName");
            Log.d("MINfhvbueihb", p);
            uimg = tinyDB.getString("updated_image");
            uUsername = tinyDB.getString("updated_email");
            //   Log.e("UpdatedImage", uimg);

//        Toast.makeText(MainActivity.this,""+p +u,Toast.LENGTH_SHORT).show();
            if (p.isEmpty()) {
                circularImageView.setImageResource(R.drawable.guru_pic);
            } else {
                Picasso.with(MainActivity.this).load(p).error(R.drawable.place_holder).into(circularImageView);
            }

            navigation_drawer_text.setText(u);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
                Picasso.Builder builder = new Picasso.Builder(this);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                        System.out.println("IMAeopgihyuseu7hobGE" + exception);
                    }
                });
                builder.build().load(profileImg).error(R.drawable.place_holder).into(circularImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            String profile_Img = tinyDB.getString("updated_image");
            String profileEmail = tinyDB.getString("updated_email");
            System.out.println("IMAeopgihyuseu7hobGE" + profile_Img + profileEmail);

            if (profile_Img.isEmpty()) {
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
                    builder.build().load(profile_Img).into(circularImageView);
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

        apiIntroduction();

    }

    @OnClick(R.id.imgSettings)
    void settings() {
        Intent profile = new Intent(ac, Profile.class);
        startActivity(profile);
    }

    @OnClick(R.id.imgSignOut)
    void signOut() {
        tinyDB.clear();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void apiIntroduction() {
        //    prog.progDialog(ac);
        MainApplication.getApiService().aboutusRequestmethod("text/plain").enqueue(new Callback<IntroResponse>() {
            @Override
            public void onResponse(Call<IntroResponse> call, Response<IntroResponse> response) {
                //    prog.hideProg();
                IntroResponse introResponse = response.body();
                if (response.body().getRespCode().matches("2007")) {
                    tvTitle.setText(introResponse.getTitle() + " 1");
                    tvSubTitle.setText(introResponse.getSubtitle());
                    tvIntro.setText(introResponse.getIntro());
                } else {

                }
            }

            @Override
            public void onFailure(Call<IntroResponse> call, Throwable t) {

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            String profileImg = tinyDB.getString("updated_image");
            String profileEmail = tinyDB.getString("updated_email");
            System.out.println("IMAeopgihyuseu7hobGE" + profileImg + profileEmail);

            if (profileImg.isEmpty()) {
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
                    builder.build().load(profileImg).error(R.drawable.place_holder).into(circularImageView);
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
    }


    public static void onMessageStatusbarIndication(boolean b) {
        statusBarHit = b;
        //redbarTv.callOnClick();
        try{
            tvSubTitle2.callOnClick();
        }catch (Exception exc){
            exc.printStackTrace();
        }


       /* try {
            if (b) {
                redbarTv.setVisibility(View.VISIBLE);

            } else {
                redbarTv.setVisibility(View.GONE);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }*/

        /* Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            statusBarHit = !statusBarHit;
                    //redbarTv.setVisibility(statusBarHit ? View.GONE:View.VISIBLE);
                    if(statusBarHit)
                    {
                        redbarTv.setVisibility(View.VISIBLE);
                    }else {
                        redbarTv.setVisibility(View.GONE);
                    }

                    window.setStatusBarColor(statusBarHit ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.redStatusBar));
                }*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        statusBarHit =false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusBarHit =false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }
 /* Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            statusBarHit = !statusBarHit;
                    //redbarTv.setVisibility(statusBarHit ? View.GONE:View.VISIBLE);
                    if(statusBarHit)
                    {
                        redbarTv.setVisibility(View.VISIBLE);
                    }else {
                        redbarTv.setVisibility(View.GONE);
                    }

                    window.setStatusBarColor(statusBarHit ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.redStatusBar));
                }*/
}
