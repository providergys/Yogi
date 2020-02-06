package com.yogi_app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yogi_app.R;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.model.LoginResponse;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.SnakeBaar;
import com.yogi_app.utility.TinyDB;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    TinyDB tinyDB;  Integer userID;
    SnakeBaar snakeBaar = new SnakeBaar();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);
        userID = tinyDB.getInt("user_id");
    }

    @OnClick(R.id.btnSubmit)
    void submit(){
        apiChangePassword();
    }
    @OnClick(R.id.imgBackPress)
    void BackPress(){
        onBackPressed();
    }
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    public void apiChangePassword() {
        final FaqResponse request = new FaqResponse();
        request.setUserId(userID);
        if(password.getText().toString().equals(etConfirmPassword.getText().toString())){
            request.setPassword(etConfirmPassword.getText().toString());
        }else  if(password.getText().toString().equals(" ")||etConfirmPassword.getText().toString().equals(" ")){
             snakeBaar.showSnackBar(ChangePassword.this,"Fields are empty.",linearLayout);
        }
        else {
           // snakeBaar.showSnackBar(ChangePassword.this,"password fields are not matched.",linearLayout);

        }
        MainApplication.getApiService().changePassword("text/plain", request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                LoginResponse profileResponse = response.body();
                if (response.body().getRespCode().matches("7005")) {

                }
                else {
                    snakeBaar.showSnackBar(ChangePassword.this,profileResponse.getMessage(),linearLayout);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                snakeBaar.showSnackBar(ChangePassword.this,t.getMessage(),linearLayout);

            }
        });
    }


}
