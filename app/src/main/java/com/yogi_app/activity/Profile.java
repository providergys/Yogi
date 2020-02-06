package com.yogi_app.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.yogi_app.R;
import com.yogi_app.adapters.ContactusAdapter;
import com.yogi_app.adapters.ProfileOrdinationAdapter;
import com.yogi_app.alipay.OrdinationLevels;
import com.yogi_app.alipay.UserOrdinationInfo;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.LoginResponse;
import com.yogi_app.model.ProfileOrdination;
import com.yogi_app.model.ProfileOrdinationModel;
import com.yogi_app.model.ProfileUserInforResponse;
import com.yogi_app.model.User;
import com.yogi_app.retroutility.MainApplication;

import com.yogi_app.utility.Base64;
import com.yogi_app.utility.SelectImagePermissions;
import com.yogi_app.utility.SnakeBaar;
import com.yogi_app.utility.TinyDB;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    TinyDB tinyDB;
    CircularImageView imageView;
    EditText edtFirstName, edtLastName, edtEmail, edtMobile, edtDharmaName;
    Button btnSubmit, btnChangePAssword;
    Integer userID;
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String photo_value;
    RelativeLayout rel_pro;
    Bitmap selectFile;
    ProfileOrdinationAdapter adapter;
    List<ProfileOrdination> list = new ArrayList<>();
    SnakeBaar snakeBaar = new SnakeBaar();
    RadioButton radioYes, radioNo;
    RadioButton radioSingapore, radioEastMalaysia, radioWestMalaysia, radioChina, radioIndonesia;
    RadioGroup radioGroupResidence;
    String langval;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    CheckBox chkboxSingapore1, chkboxEastMalaysia1, chkboxWestMalaysia1, chkboxChina1, chkboxIndonesia1;
    List<ProfileUserInforResponse> countryList = new ArrayList<>();
    List<ProfileUserInforResponse> ordinationList = new ArrayList<>();
    RecyclerView recycler, recyclerCountryList, recyclerCountryResidence;
    CountryResidenceAdapter countryResidenceAdapter;
    CountryAdapter countryAdapter;
    List<String> selectedCountriesList = new ArrayList<>();
    List<String> selectedOrdinationList = new ArrayList<>();
    String myFormat = "MM/dd/yy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    int status;
    String countryResidence;
    List<UserOrdinationInfo> userOrdinationInfo = new ArrayList<>();
    List<ProfileUserInforResponse> setttt = new ArrayList<>();
    List<ProfileUserInforResponse> settttgetttt = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);


        userID = tinyDB.getInt("user_id");
        recyclerCountryList = (RecyclerView) findViewById(R.id.recyclerCountryList);
        recyclerCountryResidence = (RecyclerView) findViewById(R.id.recyclerCountryResidence);
        imageView = (CircularImageView) findViewById(R.id.drawer_profile_image);
        rel_pro = (RelativeLayout) findViewById(R.id.rel_pro);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtDharmaName = (EditText) findViewById(R.id.edtDharmaName);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnChangePAssword = (Button) findViewById(R.id.btnChangePassword);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        radioYes = (RadioButton) findViewById(R.id.radioYes);
        radioNo = (RadioButton) findViewById(R.id.radioNo);
        radioYes.setOnClickListener(this);
        radioNo.setOnClickListener(this);

        if (radioYes.isChecked()) {
            status = 1;
        } else {
            status = 0;
        }

        recycler.setVisibility(View.GONE);
        apiGetProfileData();


    }


//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        boolean checked = ((CheckBox) buttonView).isChecked();
//        switch (buttonView.getId()) {
//            case R.id.chkboxSingapore1:
//                if (isChecked) {
////                    Toast.makeText(Profile.this, "ChkSingapore", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.chkboxEastMalaysia1:
//                if (isChecked) {
////                    Toast.makeText(Profile.this, "ChkboxEastMalaysia1", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.chkboxWestMalaysia1:
//                if (isChecked) {
////                    Toast.makeText(Profile.this, "chkboxWestMalaysia1", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.chkboxChina1:
//                if (isChecked) {
////                    Toast.makeText(Profile.this, "chkboxChina1", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.chkboxIndonesia1:
//                if (isChecked) {
////                    Toast.makeText(Profile.this, "Indoenbjndkje", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }

    @Override
    public void onClick(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioYes:
                if (checked)
                    recycler.setVisibility(View.VISIBLE);
                break;
            case R.id.radioNo:
                if (checked)
                    recycler.setVisibility(View.GONE);
                break;
//            case R.id.radioSingapore:
//                if (checked)
////                    Toast.makeText(Profile.this, "Singapore", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.radioChina:
//                if (checked)
////                    Toast.makeText(Profile.this, "China", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.radioEastMalaysia:
//                if (checked)
////                    Toast.makeText(Profile.this, "EastMalaysia", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.radioWestMalaysia:
//                if (checked)
////                    Toast.makeText(Profile.this, "WestMalaysia", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.radioIndonesia:
//                if (checked)
////                    Toast.makeText(Profile.this, "Indonesia", Toast.LENGTH_SHORT).show();
//                break;

        }

    }

//    public void onRadioButtonClicked(View view) {
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch (view.getId()) {
//            case R.id.radioYes:
//                if (checked)
//                    recycler.setVisibility(View.VISIBLE);
//                break;
//            case R.id.radioNo:
//                if (checked)
//                    recycler.setVisibility(View.GONE);
//                break;
//        }
//    }

//    public List<ProfileOrdination> addOrdinationLevel() {
//        List<ProfileOrdination> list1 = new ArrayList<>();
//        list1.add(new ProfileOrdination(getResources().getString(R.string.yamataka), "09-09-2017"));
//        list1.add(new ProfileOrdination(getResources().getString(R.string.sixteenDroplets), "05-05-2018"));
//        list1.add(new ProfileOrdination(getResources().getString(R.string.heruka), "09-10-2018"));
//        list1.add(new ProfileOrdination(getResources().getString(R.string.nA), ""));
//        return list1;
//    }

    @OnClick(R.id.btnSubmit)
    void submit() {
        if (!validateEmail(edtEmail.getText().toString().trim())) {
            snakeBaar.showSnackBar(Profile.this, "Not a valid email address!", rel_pro);

        } else {
            apiUpdateProfileData();

        }

    }

    @OnClick(R.id.btnChangePassword)
    void changePAssword() {

        Intent intent = new Intent(Profile.this, ChangePassword.class);
        startActivity(intent);
    }

    @OnClick(R.id.tvLanguage)
    void changeLanguage() {


//        final Dialog disl = new Dialog(Profile.this);
//        disl.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        disl.setContentView(R.layout.choose_language);
//        disl.setCancelable(false);
//        disl.show();
//        final CheckBox check_box_english = (CheckBox) disl.findViewById(R.id.check_box_english);
//        final CheckBox check_box_cheinese = (CheckBox) disl.findViewById(R.id.check_box_cheinese);
//        Button submit_btn = (Button) disl.findViewById(R.id.submit_btn);
//
//        check_box_english.setChecked(true);
//        check_box_english.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                check_box_english.setChecked(true);
//                if (check_box_cheinese.isChecked()) {
//                    check_box_cheinese.setChecked(false);
//                }
//
//            }
//        });
//        check_box_cheinese.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                check_box_cheinese.setChecked(true);
//                if (check_box_english.isChecked()) {
//                    check_box_english.setChecked(false);
//                }
//            }
//        });
//        submit_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (check_box_english.isChecked()) {
//
//                    langval = "en";
//                    tinyDB.putString("lang",langval);
//                    Configuration config = getBaseContext().getResources().getConfiguration();
//                    Locale locale = new Locale(langval);
//                    Locale.setDefault(locale);
//                    config.locale = locale;
//                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//
//
//                    setContentView(R.layout.profile_screen);
//
//                } else if (check_box_cheinese.isChecked()) {
//                    langval = "zh";
//                    tinyDB.putString("lang",langval);
//                    Configuration config = getBaseContext().getResources().getConfiguration();
//                    Locale locale = new Locale(langval);
//                    Locale.setDefault(locale);
//                    config.locale = locale;
//                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//
//                    setContentView(R.layout.profile_screen);
////                                Toast.makeText(Profile.this, "work is in progress", Toast.LENGTH_SHORT).show();
//                }
//                disl.dismiss();
//            }
//        });

        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        Intent intent = new Intent(Profile.this, Profile.class);
        PendingIntent contentIntent = PendingIntent.getActivity(Profile.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);
        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setTicker("Hearty365")
                .setContentTitle("Yogi App")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setContent(remoteViews)
                .setPriority(NotificationCompat.PRIORITY_HIGH); // value = 1


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Profile.this);
        builder.setTitle("  Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = SelectImagePermissions.checkPermission(Profile.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Choose picture"), SELECT_FILE);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = null;
            Uri tempUri = null;
            imageView.setImageBitmap(mphoto);
            if (mphoto != null) {
                //tempUri = getImageUri(Profile.this, mphoto);
                bytes = new ByteArrayOutputStream();
                mphoto.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] b = bytes.toByteArray();
                photo_value = Base64.encodeBytes(b);
            }

        }

        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Log.d("Gallery.......", "areeeeee yha ayya");

            Uri uri = data.getData();
            String[] prjection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, prjection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(prjection[0]);
            String path = cursor.getString(columnIndex);
            cursor.close();
            if (selectFile != null && !selectFile.isRecycled()) {
                selectFile.recycle();
                selectFile = null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            selectFile = BitmapFactory.decodeFile(path, options);

            imageView.setImageBitmap(selectFile);

            BitMapToString(selectFile);

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void apiGetProfileData() {
        FaqResponse request = new FaqResponse();
        request.setUserId(userID);
        MainApplication.getApiService().fetchProfileData("text/plain", request).enqueue(new Callback<ProfileUserInforResponse>() {
            @Override
            public void onResponse(Call<ProfileUserInforResponse> call, Response<ProfileUserInforResponse> response) {

                System.out.println("dhtfggghfghfghfghfghfg 1" + response.body());
                ProfileUserInforResponse profileResponse = response.body();
                if (response.body().getRespCode().matches("7006")) {

                    Toast.makeText(Profile.this, "works", Toast.LENGTH_SHORT).show();
                    try {
                        if (!profileResponse.getUserData().getProfileImage().equals("")) {
                            Picasso.with(Profile.this).load(profileResponse.getUserData().getProfileImage()).error(R.drawable.place_holder).into(imageView);
                        } else {
                            Picasso.with(Profile.this).load(R.drawable.guru_pic).into(imageView);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    edtFirstName.setText(profileResponse.getUserData().getFirstname());
                    edtLastName.setText(profileResponse.getUserData().getLastname());
                    edtEmail.setText(profileResponse.getUserData().getUserEmail());
                    edtDharmaName.setText(profileResponse.getUserData().getDharmaname());
                    edtMobile.setText(profileResponse.getUserData().getMobile());

                    for (int i = 0; i < profileResponse.getUserData().getOrdinationLevel().size(); i++) {

                        setttt.add(profileResponse);
                    }
                    countryList = profileResponse.getUserData().getAllEventLocations();

                    recyclerCountryResidence.setLayoutManager(new LinearLayoutManager(Profile.this));
                    countryResidenceAdapter = new CountryResidenceAdapter(Profile.this, countryList);
                    recyclerCountryResidence.setAdapter(countryResidenceAdapter);

                    ordinationList = profileResponse.getUserData().getOrdinationLevel();
                    recycler.setLayoutManager(new LinearLayoutManager(Profile.this));
                    recycler.setAdapter(new ProfileOrdinationAdapter(ordinationList, Profile.this));

                    recyclerCountryList.setLayoutManager(new LinearLayoutManager(Profile.this));
                    countryAdapter = new CountryAdapter(Profile.this, countryList);
                    recyclerCountryList.setAdapter(countryAdapter);


                    System.out.println("dhtfggghfghfghfghfghfg 2" + ordinationList.size());

//                     selectedOrdinationList = profileResponse.getUser_data().getUser_ordination_info();


                } else {

                    snakeBaar.showSnackBar(Profile.this, "" + response.body().getMessage(), rel_pro);

                }
            }

            @Override
            public void onFailure(Call<ProfileUserInforResponse> call, Throwable t) {

                System.out.println("dhtfggghfghfghfghfghfg 3" + t);
            }
        });
    }


    public void apiUpdateProfileData() {
        User request = new User();
        request.setUserId(userID);
        request.setFirstname(edtFirstName.getText().toString());
        request.setLastname(edtLastName.getText().toString());
        request.setEmail(edtEmail.getText().toString());
        request.setUserImage(photo_value);
        request.setEventsCountryList(selectedCountriesList);
        request.setCountry_residence(countryResidence);
        request.setUser_ordination_info(userOrdinationInfo);
        MainApplication.getApiService().updateProfileData("text/plain", request).enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {

                FaqResponse profileResponse = response.body();
                if (response.body().getRespCode().matches("7005")) {
                    snakeBaar.showSnackBar(Profile.this, "" + response.body().getMessage(), rel_pro);
                    String updatedImage = profileResponse.getUser_data().getProfileImage();
                    Log.d("WOHH", updatedImage);
                    String updatedEmail = profileResponse.getUser_data().getUserEmail();
                    try {
                        tinyDB.putString("updated_image", updatedImage);
                        tinyDB.putString("updated_email", updatedEmail);
                    } catch (Exception e) {
                    }


                } else {
                    snakeBaar.showSnackBar(Profile.this, "" + response.body().getMessage(), rel_pro);
                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {


            }
        });
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] arr = baos.toByteArray();
        photo_value = android.util.Base64.encodeToString(arr, android.util.Base64.DEFAULT);
        return photo_value;
    }
//Collections.sort(temp, new Comparator<XYZBean>()
//    {
//        @Override
//        public int compare(XYZBean lhs, XYZBean rhs) {
//
//        return Integer.valueOf(lhs.getDistance()).compareTo(rhs.getDistance());
//    }
//    });
//    Collections.sort(list, new Comparator() {
//        @Override
//        public int compare(ProfileUserInforResponse o1, ProfileUserInforResponse o2) {
//            ProfileUserInforResponse p1 = (ProfileUserInforResponse) o1;
//            ProfileUserInforResponse p2 = (ProfileUserInforResponse) o2;
//            return p1.getName().compareToIgnoreCase(p2.getName());
//        }
//    });

    public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {

        Context context;
        List<ProfileUserInforResponse> list;
        private int selectedPosition = -1;// no selection by default

        public class MyViewHolder extends RecyclerView.ViewHolder {
            CheckBox chkboxCountry;

            public MyViewHolder(View view) {
                super(view);
                chkboxCountry = (CheckBox) view.findViewById(R.id.chkboxCountry);
            }
        }

        private CountryAdapter(Context context, List<ProfileUserInforResponse> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public CountryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_country_list, parent, false);

            return new CountryAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CountryAdapter.MyViewHolder holder, final int position) {
            final ProfileUserInforResponse item = list.get(position);


            holder.chkboxCountry.setText(item.getName());


            holder.chkboxCountry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedPosition = position;
                        selectedCountriesList.add(list.get(selectedPosition).getName());
                        holder.chkboxCountry.setChecked(true);
                        Log.d("seljhgdsPikdfgh", "" + selectedPosition + " " + list.get(position).getName());
                    } else {
                        holder.chkboxCountry.setChecked(false);
                        selectedPosition = -1;
                    }
                    notifyDataSetChanged();

                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class ProfileOrdinationAdapter extends RecyclerView.Adapter<ProfileOrdinationAdapter.MyViewHolder> {

        private Context context;
        private List<ProfileUserInforResponse> list;
        private int selectedPosition = -1;// no selection by default


        public class MyViewHolder extends RecyclerView.ViewHolder {
            CheckBox chkboxOrdination;
            TextView tvDate;

            public MyViewHolder(View itemView) {
                super(itemView);
                chkboxOrdination = (CheckBox) itemView.findViewById(R.id.chkboxOrdination);
                tvDate = (TextView) itemView.findViewById(R.id.tvDate);

            }
        }

        public ProfileOrdinationAdapter(List<ProfileUserInforResponse> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public ProfileOrdinationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_profile_ordination, parent, false);

            return new ProfileOrdinationAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ProfileOrdinationAdapter.MyViewHolder holder, final int position) {
            final ProfileUserInforResponse item = list.get(position);


            holder.chkboxOrdination.setText(item.getOrdinated_name());

            final Calendar myCalendar = Calendar.getInstance();

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    holder.tvDate.setText(sdf.format(myCalendar.getTime()));
                }

            };


            holder.tvDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    new DatePickerDialog(Profile.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                }
            });

            holder.chkboxOrdination.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedPosition = position;
                        //ProfileUserInforResponse endZModel = setttt.get(position).getOrdinated_name();
//setttt.get(position).getOrdinated_name()
//                       settttgetttt.add(setttt.get(position).getOrdinated_name())

                        /*
                        userOrdinationInfo.get(selectedPosition).setOrdinatedName(list.get(selectedPosition).getOrdinated_name());
                        userOrdinationInfo.get(selectedPosition).setOrdinatedStatus(Integer.valueOf(list.get(selectedPosition).getStatus()));
                        userOrdinationInfo.get(selectedPosition).setOrdinatedId(Integer.valueOf(list.get(selectedPosition).getOrdinated_id()));
                        userOrdinationInfo.get(selectedPosition).setOrdinatedDate(holder.tvDate.getText().toString());
*/

                        userOrdinationInfo.get(selectedPosition).setOrdinatedDate(holder.tvDate.getText().toString());


                        holder.chkboxOrdination.setChecked(true);
                        Log.d("seljhgdsPikdfgh", "" + selectedPosition + " " + list.get(selectedPosition));
                    } else {
                        holder.chkboxOrdination.setChecked(false);
                        selectedPosition = -1;
                    }
                    notifyDataSetChanged();

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class CountryResidenceAdapter extends RecyclerView.Adapter<CountryResidenceAdapter.MyViewHolder> {

        Context context;
        List<ProfileUserInforResponse> list;
        private int selectedPosition = -1;// no selection by default

        public class MyViewHolder extends RecyclerView.ViewHolder {
            RadioButton radioCountry;

            public MyViewHolder(View view) {
                super(view);
                radioCountry = (RadioButton) view.findViewById(R.id.radioCountry);
            }
        }

        private CountryResidenceAdapter(Context context, List<ProfileUserInforResponse> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public CountryResidenceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_country_residence, parent, false);

            return new CountryResidenceAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CountryResidenceAdapter.MyViewHolder holder, final int position) {
            final ProfileUserInforResponse item = list.get(position);

            holder.radioCountry.setText(item.getName());

            if (selectedPosition == position) {
                holder.radioCountry.setChecked(true);
            } else {
                holder.radioCountry.setChecked(false);

            }
            holder.radioCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.radioCountry.isChecked()) {
                        selectedPosition = position;
                        Log.d("seljhgdsPikdfgh", "" + selectedPosition + " " + list.get(position).getName());
                        countryResidence = list.get(position).getName();

                    } else {
                        selectedPosition = -1;
                    }
                    notifyDataSetChanged();
                }
            });

//            holder.radioCountry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if(isChecked){
//                        selectedPosition =  position;
////                        selectedCountriesList.add(list.get(selectedPosition).getName());
//                        holder.radioCountry.setChecked(true);
//                    }else {
//                        holder.radioCountry.setChecked(false);
//                        selectedPosition =  -1;
//                    }
//                    notifyDataSetChanged();
//
//                }
//            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
