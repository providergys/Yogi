package com.yogi_app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.coolerfall.widget.lunar.LunarView;
import com.coolerfall.widget.lunar.MonthDay;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.yogi_app.R;
import com.yogi_app.adapters.CalendarAdapter;
import com.yogi_app.model.CalendarRequest;
import com.yogi_app.model.CalendarResponse;
import com.yogi_app.model.DataModel;
import com.yogi_app.model.DrawerItemCustomAdapter;
import com.yogi_app.model.LoginResponse;
import com.yogi_app.model.User;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.ProgDialog;
import com.yogi_app.utility.SnakeBaar;
import com.yogi_app.utility.TinyDB;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Calender extends AppCompatActivity implements LunarView.OnDatePickListener{
    ImageView back_icon, menu_icon;
    public static DrawerLayout mDrawerLayout;
    public static DrawerItemCustomAdapter drawer_adapter;
    ListView mDrawerList;
    ImageView add_btn;
    Activity ac;
    Integer userID;
    TinyDB tinyDB;
    String calDateClicked = "";
    String calYear[] = {""};
    String finalDate = "";
    String formattedDate = "";
    SnakeBaar snakeBaar = new SnakeBaar();
    CompactCalendarView compactCalendarView;
    CalendarAdapter adapter;
    RecyclerView recycler;
    RelativeLayout rel_pro;
    @Bind(R.id.drawer_profile_image)
    CircularImageView circularImageView;
    @Bind(R.id.navigation_drawer_text)
    TextView navigation_drawer_text;
    ProgDialog progDialog = new ProgDialog();
    ImageView imgSettings, imgSignOut;
    LunarView lunarView;
    String event_title, event_date, event_desc, event_time, event_loc;
    int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        ac = this;
        ButterKnife.bind(this);

        tinyDB = new TinyDB(this);
        userID = tinyDB.getInt("user_id");
        // compactCalendarView = (CompactCalendarView) findViewById(R.id.calendar_view);
        lunarView = (LunarView) findViewById(R.id.calendar_view);
//        calendarView();

        rel_pro = (RelativeLayout) findViewById(R.id.rel_pro);
        //  add_btn = (ImageView) findViewById(R.id.add_btn);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(Calender.this));

        imgSettings = (ImageView) findViewById(R.id.imgSettings);
        imgSignOut = (ImageView) findViewById(R.id.imgSignOut);

        if (!tinyDB.getString("LOGIN").matches("user_login")) {
            imgSettings.setVisibility(View.INVISIBLE);
            imgSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Calender.this, Login.class);
                    startActivity(intent);
                }
            });
        }

//        if (tinyDB.getString("LOGIN").matches("user_login")) {
//            add_btn.setVisibility(View.VISIBLE);
//        } else {
//            add_btn.setVisibility(View.INVISIBLE);
//        }
//        add_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final Dialog dialog = new Dialog(ac);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.event_dialogue);
//
//
//                // set the custom dialog components - text, image and button
//                final EditText event_name = (EditText) dialog.findViewById(R.id.event_name);
//                Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
//                final TextView event_time = (TextView) dialog.findViewById(R.id.event_time);
//                event_time.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final Calendar c = Calendar.getInstance();
//                        final int mHour = c.get(Calendar.HOUR_OF_DAY);
//                        final int mMinute = c.get(Calendar.MINUTE);
//
//                        // Launch Time Picker Dialog
//                        TimePickerDialog timePickerDialog = new TimePickerDialog(Calender.this,
//                                new TimePickerDialog.OnTimeSetListener() {
//
//                                    @Override
//                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                        //For getting time in 12 hour format
//                                        int hour = hourOfDay;
//                                        int minutes = minute;
//                                        String timeSet = "";
//                                        if (hour > 12) {
//                                            hour -= 12;
//                                            timeSet = "PM";
//                                        } else if (hour == 0) {
//                                            hour += 12;
//                                            timeSet = "AM";
//                                        } else if (hour == 12) {
//                                            timeSet = "PM";
//                                        } else {
//                                            timeSet = "AM";
//                                        }
//
//                                        String min = "";
//                                        if (minutes < 10)
//                                            min = "0" + minutes;
//                                        else
//                                            min = String.valueOf(minutes);
//
//                                        String Hour = "";
//                                        if (hour < 10)
//                                            Hour = "0" + hour;
//                                        else
//                                            Hour = String.valueOf(hour);
//
//                                        // Append in a StringBuilder
//                                        String aTime = new StringBuilder().append(Hour).append(':')
//                                                .append(min).append(" ").append(timeSet).toString();
//                                        System.out.println("iofgwsgwsgwsgwsgwsgwsgws" + aTime);
//                                        event_time.setText(aTime);
//
//                                    }
//                                }, mHour, mMinute, false);
//                        timePickerDialog.show();
//                    }
//                });
//                final EditText event_des = (EditText) dialog.findViewById(R.id.event_des);
//                dialog.show();
//                btnSubmit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (event_name.getText().toString().equals("") || event_des.getText().toString().equals("") || event_time.getText().toString().equals("")) {
//                            snakeBaar.showSnackBar(Calender.this, "Fields are mandatory.", rel_pro);
//                        } else {
//                            getWindow().setSoftInputMode(
//                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                            apiCreateEvent(event_name.getText().toString(), event_des.getText().toString(), event_time.getText().toString());
//                            dialog.dismiss();
//                        }
//
//                    }
//                });
//
//
//            }
//        });


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

                    if (tinyDB.getString("LOGIN").matches("user_login")) {
                        Intent library = new Intent(ac, FaqActivity.class);
                        startActivity(library);
                    } else {
                        Toast.makeText(Calender.this, "Please Login...", Toast.LENGTH_SHORT).show();
                    }

                } else if (position == 4) {
//                    Intent calendar = new Intent(ac, Calender.class);
//                    startActivity(calendar);
                } else if (position == 5) {
                    Intent contactUS = new Intent(ac, ContactUs.class);
                    startActivity(contactUS);
                } else if (position == 6) {
                    if (tinyDB.getString("LOGIN").matches("user_login")) {
                        Intent playList = new Intent(ac, MyPlaylist.class);
                        startActivity(playList);
                    } else {

                        Toast.makeText(Calender.this, "Please Login...", Toast.LENGTH_SHORT).show();
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

        try {
            String p = tinyDB.getString("profile");
            String u = tinyDB.getString("userName");
            navigation_drawer_text.setText(u);

//        uimg = tinyDB.getString("updated_image");
//        uUsername = tinyDB.getString("updated_email");
            Log.e("Usdffflfl", u);
//            Toast.makeText(Calender.this, "" + p + u, Toast.LENGTH_SHORT).show();
            if (p.isEmpty()) {
                circularImageView.setImageResource(R.drawable.guru_pic);
            } else {
                Picasso.with(Calender.this).load(p).error(R.drawable.place_holder).into(circularImageView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String integratedUserName = tinyDB.getString("userName ");
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
                builder.build().load(profileImg).error(R.drawable.place_holder).into(circularImageView);
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


        apiGetEvents();

//if(lunarView.isSelected()){
//    Toast.makeText(Calender.this,"this",Toast.LENGTH_SHORT).show();
//
//}
//        lunarView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lunarView.setOnDatePickListener(Calender.this);
//            }
//        });


    }

    @Override
    public void onDatePick(LunarView view, MonthDay monthDay) {
        year = monthDay.getCalendar().get(Calendar.YEAR);
        month = monthDay.getCalendar().get(Calendar.MONTH);
        day = monthDay.getCalendar().get(Calendar.DAY_OF_MONTH);
        String lunarMonth = monthDay.getLunar().getLunarMonth();
        String lunarDay = monthDay.getLunar().getLunarDay();

        formattedDate = "" + year + "-" + month + "-" + day;
        apiGetEventDate();

        //   Toast.makeText(Calender.this, "works" +year +"/" +month +"/" +day, Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.btn)
    void onclick() {
        addEventtoDevice();
    }

    private void apiGetEventDate() {
        System.out.println("gfrddddgdfgdfgdfgd" + userID);
        progDialog.progDialog(Calender.this);
        CalendarRequest request = new CalendarRequest();
        request.setUser_id(userID);
        request.setEvent_date(formattedDate);
        System.out.println("ggddatehjfjkghj" + formattedDate);
        MainApplication.getApiService().getCalendarEvents("text/plain", request).enqueue(new Callback<CalendarResponse>() {
            @Override
            public void onResponse(Call<CalendarResponse> call, Response<CalendarResponse> response) {

                System.out.println("sdhjgfysfg" + userID);

                CalendarResponse calendarResponse = response.body();
                if (response.body().getRespCode().matches("7012")) {

                    if (calendarResponse.getUserEventData() != null) {
                        Log.d("evendjkde", "" + calendarResponse.getUserEventData().get(0).getEventDate() + calendarResponse.getUserEventData().get(0).getEventTime() + calendarResponse.getUserEventData().get(0).getEventTitle() + calendarResponse.getUserEventData().get(0).getEventLocation() + calendarResponse.getUserEventData().get(0).getEventId());
//                        calendarResponse.getEvent_date();
//                        calendarResponse.getEvent_time();
//                        calendarResponse.getEvent_title();
//                        calendarResponse.getEvent_location();
//                        calendarResponse.getAbout_event();


                        adapter = new CalendarAdapter(Calender.this, calendarResponse.getUserEventData());
                        recycler.setAdapter(adapter);
                        //   Toast.makeText(Calender.this,""+calendarResponse.getUserEventData().get(0).getFavourite_or_not(),Toast.LENGTH_SHORT).show();
//                        progDialog.hideProg();


                    }

                } else {

                    if (!tinyDB.getString("LOGIN").matches("user_login")) {
                        imgSettings.setVisibility(View.INVISIBLE);
                        imgSignOut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Calender.this, Login.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        Toast.makeText(Calender.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CalendarResponse> call, Throwable t) {

            }
        });
    }

    private void apiGetEvents() {
        System.out.println("gfrddddgdfgdfgdfgd" + userID);
        progDialog.progDialog(Calender.this);
        CalendarRequest request = new CalendarRequest();
        request.setUser_id(userID);
        MainApplication.getApiService().getCalendarEvents("text/plain", request).enqueue(new Callback<CalendarResponse>() {
            @Override
            public void onResponse(Call<CalendarResponse> call, Response<CalendarResponse> response) {

                System.out.println("sdhjgfysfg" + userID);

                CalendarResponse calendarResponse = response.body();
                if (response.body().getRespCode().matches("7012")) {

                    if (calendarResponse.getUserEventData() != null) {
                        Log.d("evendjkde", "" + calendarResponse.getUserEventData().get(0).getEventDate() + calendarResponse.getUserEventData().get(0).getEventTime() + calendarResponse.getUserEventData().get(0).getEventTitle() + calendarResponse.getUserEventData().get(0).getEventLocation() + calendarResponse.getUserEventData().get(0).getEventId());
//                        calendarResponse.getEvent_date();
//                        calendarResponse.getEvent_time();
//                        calendarResponse.getEvent_title();
//                        calendarResponse.getEvent_location();
//                        calendarResponse.getAbout_event();

                        for(int i=0; i<calendarResponse.getUserEventData().size();i++){
                            event_time = calendarResponse.getUserEventData().get(i).getEventTime();
                            event_date = calendarResponse.getUserEventData().get(i).getEventDate();
                            event_title = calendarResponse.getUserEventData().get(i).getEventTitle();
                            event_loc = calendarResponse.getUserEventData().get(i).getEventLocation().get(0).getLocationName();
                            event_desc = calendarResponse.getUserEventData().get(i).getEventDesc();

                        }

                        adapter = new CalendarAdapter(Calender.this, calendarResponse.getUserEventData());
                        recycler.setAdapter(adapter);
                        //   Toast.makeText(Calender.this,""+calendarResponse.getUserEventData().get(0).getFavourite_or_not(),Toast.LENGTH_SHORT).show();
                        progDialog.hideProg();


                    }

                } else {

                    if (!tinyDB.getString("LOGIN").matches("user_login")) {
                        imgSettings.setVisibility(View.INVISIBLE);
                        imgSignOut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Calender.this, Login.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        Toast.makeText(Calender.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CalendarResponse> call, Throwable t) {

            }
        });
    }

    private void calendarView() {
//        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
//        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
//            @Override
//            public void onDayClick(Date dateClicked) {
//                Date date2 = null;
//                String s = dateClicked.toString();
//                String[] date = s.split("00:00:00 ");
//
//                calDateClicked = date[0];
//
//                calYear = date[1].split(" ");
//                String date1[] = calDateClicked.split(" ");
//
//                finalDate = calYear[1] + "-" + date1[1] + "-" + date1[2];
//                DateFormat originalFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH);
//                DateFormat targetFormat = new SimpleDateFormat("yyyy/MM/dd");
//                try {
//                    date2 = originalFormat.parse(finalDate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                formattedDate = targetFormat.format(date2);
//               // Toast.makeText(getApplicationContext(), "Date " + formattedDate
//                       // , Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onMonthScroll(Date firstDayOfNewMonth) {
//                Log.d("MonthScrolled", "" + firstDayOfNewMonth);
//            }
//        });



    }


    private void apiCreateEvent(String event_name, String event_des, String event_time) {
        CalendarRequest request = new CalendarRequest();
        request.setUser_id(userID);
        request.setAbout_event(event_des);
        request.setEvent_title(event_name);
        request.setEvent_date(formattedDate);
        request.setEvent_time(event_time);
        MainApplication.getApiService().addCalendarEvent("text/plain", request).enqueue(new Callback<CalendarResponse>() {
            @Override
            public void onResponse(Call<CalendarResponse> call, Response<CalendarResponse> response) {

                CalendarResponse profileResponse = response.body();
                if (response.body().getRespCode().matches("7009")) {
                    Toast.makeText(Calender.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    apiGetEvents();
                    //   snakeBaar.showSnackBar(Calender.this,""+response.body().getMessage(),rel_pro);
                } else {
                    Toast.makeText(Calender.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //   snakeBaar.showSnackBar(Calender.this,""+response.body().getMessage(),rel_pro);
                }
            }

            @Override
            public void onFailure(Call<CalendarResponse> call, Throwable t) {

                snakeBaar.showSnackBar(Calender.this, "" + t.getMessage(), rel_pro);
            }
        });

    }

    public void addEventtoDevice() {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event_time);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, "");
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        intent.putExtra(CalendarContract.Events.TITLE, event_title);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, event_desc);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event_loc);
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");
        startActivity(intent);
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
