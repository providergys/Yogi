package com.yogi_app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;
import com.squareup.picasso.Picasso;
import com.yogi_app.R;
import com.yogi_app.adapters.ExpandlListAdapter;
import com.yogi_app.model.DataModel;
import com.yogi_app.model.DrawerItemCustomAdapter;
import com.yogi_app.model.FaqRequest;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.IntroResponse;
import com.yogi_app.model.LoginRequest;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.ProgDialog;
import com.yogi_app.utility.SnakeBaar;
import com.yogi_app.utility.TinyDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqActivity extends Activity {

    private Map<String, List<FaqResponse>> categoryMap;
    private List<FaqResponse> parentList;
    private ExpandablePlaceHolderView expandablePlaceHolderView;
    Activity ac;
    ImageView back_icon, menu_icon;
    ListView mDrawerList;
    TinyDB tinyDB;
    public static DrawerLayout mDrawerLayout;
    public static DrawerItemCustomAdapter drawer_adapter;
    @Bind(R.id.etQuestion)
    EditText etQuestion;
    Button btnSend;
    RelativeLayout layout_relative;
    SnakeBaar snakeBaar = new SnakeBaar();
    @Bind(R.id.drawer_profile_image)
    CircularImageView circularImageView;
    @Bind(R.id.navigation_drawer_text)
    TextView navigation_drawer_text;
    ProgDialog progDialog = new ProgDialog();
    ImageView imgSettings, imgSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ac = this;
        ButterKnife.bind(ac);
        tinyDB = new TinyDB(ac);
        imgSettings = (ImageView)findViewById(R.id.imgSettings);
        imgSignOut = (ImageView)findViewById(R.id.imgSignOut);


        if(tinyDB.getString("LOGIN").matches("user_login")){
            imgSettings.setVisibility(View.VISIBLE);
            imgSignOut.setVisibility(View.VISIBLE);
        }else{
            imgSettings.setVisibility(View.INVISIBLE);
            imgSignOut.setVisibility(View.INVISIBLE);
        }

        layout_relative = (RelativeLayout) findViewById(R.id.layout_relative);
        btnSend = (Button) findViewById(R.id.btnSend);
        final View view = this.getCurrentFocus();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getWindow().setSoftInputMode(
//                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                );
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                apiSaveFaqQuestions();
                etQuestion.setText("");

            }
        });

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
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
            }
        });

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

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    System.out.println("ewlfkfdsfd" + " hua hai ase");

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

                        Toast.makeText(FaqActivity.this,"Please Login...",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(FaqActivity.this,"Please Login...",Toast.LENGTH_SHORT).show();
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

        parentList = new ArrayList<>();
        categoryMap = new HashMap<>();
        Collections.sort(parentList, new Comparator<FaqResponse>() {
            @Override
            public int compare(FaqResponse lhs, FaqResponse rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        expandablePlaceHolderView = (ExpandablePlaceHolderView) findViewById(R.id.placeHolder);
        loadData();

        String p = tinyDB.getString("profile");
        String u = tinyDB.getString("userName");

//        uimg = tinyDB.getString("updated_image");
//        uUsername = tinyDB.getString("updated_email");
//        Log.e("UpdatedImage", uimg);
//        Toast.makeText(MainActivity.this,""+p +u,Toast.LENGTH_SHORT).show();
        if (p.isEmpty()) {
            circularImageView.setImageResource(R.drawable.guru_pic);
        } else {
            Picasso.with(FaqActivity.this).load(p).into(circularImageView);
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
                    builder.build().load(profile_img).into(circularImageView);
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
    }

    private void loadData() {
        //GETTING Questions in list form
        apiFetchFAQS();
    }

    void apiSaveFaqQuestions() {
        FaqRequest faqRequest = new FaqRequest();
        faqRequest.setUserId(String.valueOf(tinyDB.getInt("user_id")));
        faqRequest.setQuestion(etQuestion.getText().toString());

        MainApplication.getApiService().faq_method("text/plain", faqRequest).enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                if (response.body().getRespCode().matches("2006")) {

                    snakeBaar.showSnackBar(FaqActivity.this, "" + response.body().getMessage(), layout_relative);
                    apiFetchFAQS();
                } else {

                    snakeBaar.showSnackBar(FaqActivity.this, "" + response.body().getMessage(), layout_relative);
                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {

            }
        });
    }

    public void apiFetchFAQS() {
        progDialog.progDialog(FaqActivity.this);
        MainApplication.getApiService().fetchFAQS("text/plain").enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                parentList.clear();
                FaqResponse faqResponse = response.body();
                if (response.body().getRespCode().matches("2009")) {
                    parentList = faqResponse.getList();
//                    if(parentList==null){
//                    } else {
                    getHeaderAndChild(parentList);
//                    }
                    //   snakeBaar.showSnackBar(FaqActivity.this,""+response.body().getMessage(),layout_relative);
                    progDialog.hideProg();
                } else {

                    //   Toast.makeText(ac, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    snakeBaar.showSnackBar(FaqActivity.this, "" + response.body().getMessage(), layout_relative);
                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {

            }
        });
    }

    private void getHeaderAndChild(List<FaqResponse> list) {
        for (FaqResponse item : parentList) {
            List<FaqResponse> movieList1 = categoryMap.get(item.getAnswer());
            if (movieList1 == null) {
                movieList1 = new ArrayList<>();
            }
            movieList1.add(item);
            categoryMap.put(item.getQuestion(), movieList1);
        }
        Log.d("Map", categoryMap.toString());
        Iterator it = categoryMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Log.d("Key", pair.getKey().toString());
            expandablePlaceHolderView.addView(new HeaderView(this, pair.getKey().toString()));
            List<FaqResponse> movieList1 = (List<FaqResponse>) pair.getValue();
            for (FaqResponse movie : movieList1) {
                expandablePlaceHolderView.addView(new ChildView(this, movie));
            }
            it.remove();
        }
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
