package com.yogi_app.activity;

/**
 * Created by Andriod on 5/31/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yogi_app.R;
import com.yogi_app.adapters.ProfileOrdinationAdapter;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.ProfileOrdination;
import com.yogi_app.model.SignUpRequest;
import com.yogi_app.model.SignUpResponse;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.SnakeBaar;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Signup extends Activity {
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    Activity ac;
    SnakeBaar snakeBaar = new SnakeBaar();

    @Bind(R.id.rel_layout)
    RelativeLayout rel_layout;
    @Bind(R.id.user_name)
    EditText user_name;
    @Bind(R.id.user_email)
    EditText user_email;
    @Bind(R.id.user_password)
    EditText user_password;
    @Bind(R.id.contactNo)
    EditText contactNo;
    @Bind(R.id.btn_sign_up)
    Button btn_sign_up;
    @Bind(R.id.signupheader)
    TextView signupheader;
    @Bind(R.id.already_account_txt)
    TextView already_account_txt;
    @Bind(R.id.sign_in)
    TextView sign_in;
    ImageView imgBackPress;
    private static final String TAG = "Signup";
    Typeface mFont;
    RadioButton radioYes, radioNo;
    RecyclerView recycler;
    List<FaqResponse> list = new ArrayList<>();
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        ac = this;

        ButterKnife.bind(ac);

        mFont = Typeface.createFromAsset(getAssets(), "robotlight.ttf");

        user_email.setTypeface(mFont);
        user_password.setTypeface(mFont);
        contactNo.setTypeface(mFont);
        btn_sign_up.setTypeface(mFont);
        user_name.setTypeface(mFont);
        signupheader.setTypeface(mFont);
        already_account_txt.setTypeface(mFont);
        sign_in.setTypeface(mFont);
        imgBackPress = (ImageView) findViewById(R.id.imgBackPress);
        imgBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        radioYes = (RadioButton) findViewById(R.id.radioYes);
        radioNo = (RadioButton) findViewById(R.id.radioNo);


//        btn_sign_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               String email = user_email.getText().toString();
//                String password = user_password.getText().toString();
//                String con_password= re_user_password.getText().toString();
//                if (!validateEmail( email )) {
//                   snakeBaar.showSnackBar( ac, "Not a valid email address!", login_Layout );
//
//                }else if (password.equals( "" )) {
//                   snakeBaar.showSnackBar( ac, "Password can not be empty", login_Layout );
//
//
//                } else if (con_password.equals( "" )) {
//
//                    snakeBaar.showSnackBar( ac, "Confirm password can not be empty", login_Layout );
//
//                } else if (!con_password.matches( password )) {
//
//
//                    snakeBaar.showSnackBar( ac, "Password & Confirm Password mismatch", login_Layout );
//
//                }else {
//
//
////                    retrofitRegistration();
//
//                }
//            }
//        });

        recycler = (RecyclerView) findViewById(R.id.recycler);
//        list = addOrdinationLevel();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new ProfileOrdinationAdapter(list, Signup.this));
        recycler.setVisibility(View.GONE);

        radioYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onRadioButtonClicked(v);
            }
        });

        radioNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onRadioButtonClicked(v);

            }
        });


    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
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
        }
    }

//    public List<FaqResponse> addOrdinationLevel() {
//        List<FaqResponse> list1 = new ArrayList<>();
//        list1.add(new ProfileOrdination(getResources().getString(R.string.yamataka), "09-09-2017"));
//        list1.add(new ProfileOrdination(getResources().getString(R.string.sixteenDroplets), "05-05-2018"));
//        list1.add(new ProfileOrdination(getResources().getString(R.string.heruka), "09-10-2018"));
//        list1.add(new ProfileOrdination(getResources().getString(R.string.nA), ""));
//        return list1;
//    }

    @OnClick(R.id.sign_in)
    void signIn() {
        Intent intent = new Intent(Signup.this, Login.class);
        startActivity(intent);
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @OnClick(R.id.btn_sign_up)
    void btnSignUp() {
        String email = user_email.getText().toString();
        String password = user_password.getText().toString();
        String con_password = contactNo.getText().toString();
//        if (!validateEmail( email )) {
//            snakeBaar.showSnackBar( ac, "Not a valid email address!", login_Layout );
//
//        }else if (password.equals( "" )) {
//            snakeBaar.showSnackBar( ac, "Password can not be empty", login_Layout );
//
//
//        } else if (con_password.equals( "" )) {
//
//            snakeBaar.showSnackBar( ac, "Confirm password can not be empty", login_Layout );
//
//        } else if (!con_password.matches( password )) {
//
//
//            snakeBaar.showSnackBar( ac, "Password & Confirm Password mismatch", login_Layout );
//
//        }else {


        apiSignUp();

        //  }
    }

    public void apiSignUp() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(user_name.getText().toString());
        signUpRequest.setEmail(user_email.getText().toString());
        //
        // if(user_password.getText().toString().equals(contactNo.getText().toString())){
        signUpRequest.setPassword(user_password.getText().toString());
//        }
//        else {
//            snakeBaar.showSnackBar(Signup.this,"password not matched.",rel_layout);
//        }
        signUpRequest.setContactNo(contactNo.getText().toString());

        MainApplication.getApiService().signup("text/plain", signUpRequest).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.body().getRespCode().matches("1004")) {
                    snakeBaar.showSnackBar(Signup.this, response.body().getMessage(), rel_layout);
                } else {
                    // Toast.makeText(Signup.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                snakeBaar.showSnackBar(Signup.this, t.getMessage(), rel_layout);

            }
        });
    }
//    public void retrofitRegistration() {
//
//        progressDialog= ProgressGlobal.showProgressBar(ac);
//        System.out.println("fhhhhhhhhhhhhhhhhhhh"+tele_code);
//        registerrequest.setEmail(emailText.getText().toString());
//        registerrequest.setName(nameText.getText().toString());
//        registerrequest.setContact(mobile_no.getText().toString());
//        registerrequest.setPassword(passwordText.getText().toString());
//        registerrequest.setTeleCode(tele_code);
//
//        MainApplication.getApiService().registerRequestmethod("text/plain", registerrequest).enqueue(new Callback<RegisterResponse>() {
//            @Override
//            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
//
//                if (response.isSuccessful())
//                {
//
//                    progressDialog.dismiss();
//
//
//                    if (response.body().getRespCode().matches("1004")) {
//                        snakeBaar.showSnackBar(ac, response.body().getMessage(), coordinatorLayout);
//
//
//                    } else if (response.body().getRespCode().matches("1005") || response.body().getRespCode().matches("1006")) {
//                        snakeBaar.showSnackBar(ac, response.body().getMessage(), coordinatorLayout);
//                    } else if (response.body().getRespCode().matches("1007")){
//
//                        snakeBaar.showSnackBar(ac, response.body().getMessage(), coordinatorLayout);
//
//                    }else if (response.body().getRespCode().matches("1009")){
//
//                        snakeBaar.showSnackBar(ac, response.body().getMessage(), coordinatorLayout);
//
//                    }
//
//
//                } else {
//
//                    progressDialog.dismiss();
//                    snakeBaar.showSnackBar(ac, getResources().getString(R.string.response_not_success), coordinatorLayout);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RegisterResponse> call, Throwable t) {
//
//                progressDialog.dismiss();
//                snakeBaar.showSnackBar(ac, getResources().getString(R.string.response_not_success), coordinatorLayout);
//
//
//            }
//        });
//    }
}