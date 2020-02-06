package com.yogi_app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.appunite.appunitevideoplayer.PlayerActivity;
import com.jsibbold.zoomage.ZoomageView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.yogi_app.R;
import com.yogi_app.model.DataModel;
import com.yogi_app.model.DrawerItemCustomAdapter;
import com.yogi_app.model.FaqRequest;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.YogiPagerAdapter;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.ProgDialog;
import com.yogi_app.utility.SnakeBaar;
import com.yogi_app.utility.TinyDB;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GalleryActivity extends AppCompatActivity {

    String id = "";
    Activity ac;
    String uimg, uUsername;
    RecycleHorizontalAdapter recycle_horizontal_adapter;
    int selected_position;
    SnakeBaar snakeBaar = new SnakeBaar();
    RecyclerView recyler_horizonatlview, recyler_verticalview;
    int[] img_list = new int[]{R.drawable.pooja_icon, R.drawable.workshop_icon,
            R.drawable.study_icon, R.drawable.empowerment_icon, R.drawable.engage_icon, R.drawable.pooja_icon,
            R.drawable.workshop_icon, R.drawable.study_icon, R.drawable.empowerment_icon, R.drawable.engage_icon,};

    int[] img_list_icons = new int[]{R.drawable.img_icon, R.drawable.img_icon,
            R.drawable.img_icon, R.drawable.img_icon, R.drawable.img_icon, R.drawable.img_icon,
            R.drawable.img_icon, R.drawable.img_icon, R.drawable.img_icon, R.drawable.img_icon,
            R.drawable.img_icon, R.drawable.img_icon, R.drawable.img_icon, R.drawable.img_icon,
            R.drawable.img_icon, R.drawable.img_icon};
    ProgDialog prog = new ProgDialog();
    ImageView back_icon, menu_icon;
    public static DrawerLayout mDrawerLayout;
    public static DrawerItemCustomAdapter drawer_adapter;
    ListView mDrawerList;
    Typeface mFont;

    @Bind(R.id.navigation_drawer_text)
    TextView navigation_drawer_text;
    @Bind(R.id.gallery_header)
    TextView gallery_header;
    RelativeLayout rel_layout;
    TinyDB tinyDB;
    @Bind(R.id.drawer_profile_image)
    CircularImageView circularImageView;
    ImageView imgSettings, imgSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);

        ac = this;
        tinyDB = new TinyDB(ac);
        ButterKnife.bind(ac);

        imgSettings = (ImageView) findViewById(R.id.imgSettings);
        imgSignOut = (ImageView) findViewById(R.id.imgSignOut);


        if (!tinyDB.getString("LOGIN").matches("user_login")) {
            imgSettings.setVisibility(View.INVISIBLE);
            imgSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GalleryActivity.this, Login.class);
                    startActivity(intent);
                }
            });
//            imgSignOut.setVisibility(View.INVISIBLE);
        }


        mFont = Typeface.createFromAsset(getAssets(), "robotlight.ttf");
        navigation_drawer_text.setTypeface(mFont);
        gallery_header.setTypeface(mFont);


        rel_layout = (RelativeLayout) findViewById(R.id.rel_layout);
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
        recyler_horizonatlview = (RecyclerView) findViewById(R.id.recyler_horizonatlview);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ac, LinearLayoutManager.HORIZONTAL, false);
        recyler_horizonatlview.setLayoutManager(layoutManager); // set LayoutManager to RecyclerView
        recyler_verticalview = (RecyclerView) findViewById(R.id.recyler_vertical_gallery_view);
        recyler_verticalview.setHasFixedSize(true);
        recyler_verticalview.setLayoutManager(new GridLayoutManager(ac, 4));


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
        drawer_adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(drawer_adapter);

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
//                    Intent gallery = new Intent(ac, GalleryActivity.class);
//                    startActivity(gallery);
                } else if (position == 2) {
                    Intent gallery = new Intent(ac, YogiLibraryActivity.class);
                    startActivity(gallery);
                } else if (position == 3) {

                    if (tinyDB.getString("LOGIN").matches("user_login")) {
                        Intent library = new Intent(ac, FaqActivity.class);
                        startActivity(library);
                    } else {
                        Toast.makeText(GalleryActivity.this, "Please Login...", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(GalleryActivity.this, "Please Login...", Toast.LENGTH_SHORT).show();
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


        String integratedUserName = tinyDB.getString("userName");
        String profileImg = tinyDB.getString("profileImage");
        Log.e("INTIMG", profileImg);
        //Integration profile image and name in DrawerLayout
        try {
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
                    builder.build().load(profileImg).error(R.drawable.place_holder).into(circularImageView);
                } catch (Exception e) {
                    Log.e("EXCEption", e.toString());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String p = tinyDB.getString("profile");
            String u = tinyDB.getString("userName");
            System.out.println(u + "djfksggdfffffffgdf" + p);
            Log.d("getuswqtydqdfg", u);

            try {
                if (p.isEmpty() || u.isEmpty()) {
                    circularImageView.setImageResource(R.drawable.guru_pic);
                    navigation_drawer_text.setText(" ");

                } else {
                    Picasso.with(GalleryActivity.this).load(p).error(R.drawable.place_holder).into(circularImageView);
                    navigation_drawer_text.setText(u);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            }
        } catch (Exception e) {
            Log.e("EXCEption", e.toString());
            e.printStackTrace();
        }

        apiGetGalleryCategories();

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

    private void apiGetGalleryCategoriesListItems() {
        FaqResponse request = new FaqResponse();
        request.setCategoryId(id);
        MainApplication.getApiService().fetchCategoriesListItems("text/plain", request).enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                FaqResponse faqResponse = response.body();
                if (response.body().getRespCode().matches("2017")) {
                    RecycleverticalAdapter recycle_vertical_adapter = new RecycleverticalAdapter(ac, faqResponse.getItem_array_List());
                    recyler_verticalview.setAdapter(recycle_vertical_adapter);
                    prog.hideProg();

                } else {
                    recyler_verticalview.setAdapter(null);
                    snakeBaar.showSnackBar(ac, "" + response.body().getMessage(), rel_layout);
                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {

            }
        });
    }

    public void apiGetGalleryCategories() {
        prog.progDialog(ac);
        MainApplication.getApiService().fetchCategories("text/plain").enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {

                if (response.body().getRespCode().matches("2015")) {
                    //     snakeBaar.showSnackBar(ac,""+response.body().getMessage(),layoutRelative);
                    FaqResponse faqResponse = response.body();
                    id = faqResponse.getListCategories().get(0).getId();
                    apiGetGalleryCategoriesListItems();
                    recyler_verticalview.setAdapter(null);
                    recycle_horizontal_adapter = new RecycleHorizontalAdapter(ac, faqResponse.getListCategories());
                    recyler_horizonatlview.setAdapter(recycle_horizontal_adapter);

                } else {
                    snakeBaar.showSnackBar(ac, "" + response.body().getMessage(), rel_layout);
                    //   snakeBaar.showSnackBar(ac,""+response.body().getMessage(),layoutRelative);
                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {

            }
        });
    }


    public class RecycleHorizontalAdapter extends RecyclerView.Adapter<RecycleHorizontalAdapter.MyViewHolder> {

        Context context;
        List<FaqResponse> list;
        boolean flag = true;

        public RecycleHorizontalAdapter(Context c, List<FaqResponse> list) {
            this.context = c;
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_horizontal_row, parent, false);
            MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final MyViewHolder viewHolder = (MyViewHolder) holder;

            final FaqResponse item = list.get(position);
            //	viewHolder.txt.setTypeface(mFont);
//			viewHolder.txt.setText(test_list.get(position));
            viewHolder.imgPuja.setImageResource(img_list[position]);
            Typeface mFont = Typeface.createFromAsset(getAssets(), "robotlight.ttf");

            viewHolder.txt_header.setText(item.getName());
            viewHolder.txt_header.setTypeface(mFont);
            if (selected_position == position) {
                //changes background color of selected item in RecyclerView
                viewHolder.layoutLinear.setBackgroundColor(getResources().getColor(R.color.logincolor));
            } else {
                viewHolder.layoutLinear.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                //this updated an order property by status in DB
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = item.getId();
                    selected_position = position;
                    recycle_horizontal_adapter.notifyDataSetChanged();
                    apiGetGalleryCategoriesListItems();
                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imgPuja;
            TextView txt_header;
            RecyclerView recyler_horizontalview;
            LinearLayout layoutLinear;

            public MyViewHolder(View itemView) {
                super(itemView);
                // get the reference of item view's

                imgPuja = (ImageView) itemView.findViewById(R.id.imgPuja);
                layoutLinear = (LinearLayout) itemView.findViewById(R.id.layoutLinear);
                txt_header = (TextView) itemView.findViewById(R.id.txt_header);
            }
        }
    }

    public class RecycleverticalAdapter extends RecyclerView.Adapter<RecycleverticalAdapter.MyViewHolder> {

        Context context;
        VideoView simpleVideoView;
        ZoomageView img;
        ImageView video_play_pause;
        List<FaqResponse> list;
        /**/

        public RecycleverticalAdapter(Context c, List<FaqResponse> list) {
            this.context = c;
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_items_row, parent, false);
            MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final MyViewHolder viewHolder = (MyViewHolder) holder;
            //  viewHolder.img.setImageResource(img_list_icons[position]);
            final FaqResponse item = list.get(position);
            Typeface mFont = Typeface.createFromAsset(getAssets(), "robotlight.ttf");
            viewHolder.tvTitle.setTypeface(mFont);
            viewHolder.tvTitle.setText(item.getAttachmentTitle());

            if (item.getAttachmentType().equals("image")) {
                Picasso.with(context).load(R.drawable.img_icon).into(viewHolder.img);
                viewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(ac, android.R.style.TextAppearance_Theme);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        // dialog.setCancelable(false);
                        dialog.setContentView(R.layout.image_activity);


                        final ImageView image = (ImageView) dialog.findViewById(R.id.imgView);
                        final ImageView back_icon = (ImageView) dialog.findViewById(R.id.imgBack);

                        back_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        Picasso.with(context).load(item.getAttachmentUrl()).into(image);
                        dialog.show();

                    }
                });
            } else if (item.getAttachmentType().equals("document")) {
                Picasso.with(context).load(R.drawable.doc_icon).into(viewHolder.img);
                viewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DocsURL.class);
                        intent.putExtra("DOCURL", "" + item.getAttachmentUrl());
                        Log.d("URL", item.getAttachmentUrl());
                        context.startActivity(intent);
                    }
                });

            } else if (item.getAttachmentType().equals("pdf")) {
                Picasso.with(context).load(R.drawable.pdf_icon).into(viewHolder.img);
                viewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DocsURL.class);
                        intent.putExtra("DOCURL", "" + item.getAttachmentUrl());
                        //          Log.d("URL",item.getAttachmentUrl());
                        context.startActivity(intent);
                    }
                });

            } else {
                Picasso.with(context).load(R.drawable.ic_video_img).into(viewHolder.img);
                viewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(PlayerActivity.getVideoPlayerIntent(context, item.getAttachmentUrl(), item.getTitle()));
                    }
                });

            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    final Dialog dialog = new Dialog(context, android.R.style.TextAppearance_Theme);
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    // dialog.setCancelable(false);
//                    dialog.setContentView(R.layout.img_preview);
//                    dialog.show();
//
//                    ImageView back_icon = (ImageView) dialog.findViewById(R.id.back_icon);
//                    simpleVideoView = (VideoView) dialog.findViewById(R.id.simpleVideoView);
//                    img = (ZoomageView) dialog.findViewById(R.id.myZoomageView);
//                    video_play_pause = (ImageView) dialog.findViewById(R.id.video_play_pause);
//
//                    simpleVideoView.setVisibility(View.VISIBLE);
////                    video_play_pause.setVisibility(View.VISIBLE);
//                    String uriPath = "android.resource://" + getPackageName() + "/raw/video_sample.mp4";
//                    Uri UrlPath = Uri.parse(uriPath);
//                    MediaController mediaController = new MediaController(ac);
//                    mediaController.setAnchorView(simpleVideoView);
//                    simpleVideoView.setMediaController(mediaController);
//                    simpleVideoView.setVideoURI(UrlPath);
//
//
////                    simpleVideoView.setVideoPath(R.raw.video_sample);
//                    simpleVideoView.start();


//            if(file_type.matches("videos")){
//                try {
//                    simpleVideoView.setVisibility(View.VISIBLE);
//                    video_play_pause.setVisibility(View.VISIBLE);
//                    simpleVideoView.setVideoPath(media);
//                    simpleVideoView.start();
//                }catch (Exception e){}
//            }else if(file_type.matches("images")){
//                img.setVisibility(View.VISIBLE);
//                try {
//                    Picasso.with(getActivity()).load(media.replace(" ", "%20"))
//                            .placeholder( R.drawable.guru_pic)
//                            .into(img);
//
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }


//        back_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//
//            }
//        });


                }
            });


        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView img;
            TextView tvTitle;

            public MyViewHolder(View itemView) {
                super(itemView);
                // get the reference of item view's

                img = (ImageView) itemView.findViewById(R.id.imge_view);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent newIntent = new Intent(ac, MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        super.onBackPressed();
    }
}
