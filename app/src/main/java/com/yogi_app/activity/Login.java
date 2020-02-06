package com.yogi_app.activity;

/**
 * Created by Andriod on 5/31/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookContentProvider;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.internal.FacebookInitProvider;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.iid.FirebaseInstanceId;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;
import com.yogi_app.R;
import com.yogi_app.model.ForgotpasswdRequest;
import com.yogi_app.model.ForgotpasswdResponse;
import com.yogi_app.model.LoginRequest;
import com.yogi_app.model.LoginResponse;
import com.yogi_app.model.SocialLoginRequest;
import com.yogi_app.model.SocialLoginResponse;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.IsInternetOnGlobal;
import com.yogi_app.utility.ProgDialog;
import com.yogi_app.utility.SnakeBaar;
import com.yogi_app.utility.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.telephony.TelephonyManager;
import android.content.Context;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static final int REQUEST_CODE = 1;
    private Matcher matcher;
    Activity ac;
    public static Integer userID;
    SnakeBaar snakeBaar = new SnakeBaar();
    boolean is_logged_in, rememeber_me;
    ProgDialog prog = new ProgDialog();
    @Bind(R.id.login_Layout)
    RelativeLayout login_Layout;
    @Bind(R.id.user_email)
    EditText user_email;
    @Bind(R.id.user_password)
    EditText user_password;
    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.btn_Guest)
    Button btn_Guest;
    @Bind(R.id.btn_sign_up)
    TextView btn_sign_up;
    @Bind(R.id.connect_us)
    TextView connect_us;
    private static LineApiClient lineApiClient;
    @Bind(R.id.forgotpassword)
    TextView forgotpassword;
    EditText editTextConfirm;
    @Bind(R.id.or_txt)
    TextView or_txt;
    private AccessToken accessToken;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = Login.class.getSimpleName();
    private static final String EMAIL = "email";
    LoginButton facebook_login;
    ImageView google_plus_sign_in, line_login;
    CallbackManager callbackManager;
    Typeface mFont;
    String CHANNEL_ID = "1590284140";
    TinyDB tinydb;
    private static final int RC_SIGN_IN = 007;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static String social_email, social_username, social_id, social_profile;
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ac = this;
        tinydb = new TinyDB(ac);
        token  = FirebaseInstanceId.getInstance().getToken();
        try {  // to get key hash in Facebook
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("HASH", "" + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
        FacebookSdk.sdkInitialize(ac);
        ButterKnife.bind(ac);


        if (!tinydb.getString("user_name").isEmpty()) {
            user_email.setText(String.valueOf(tinydb.getString("user_name")));
            user_password.setText(String.valueOf(tinydb.getString("password")));
        }


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

//                System.out.println("sdfdfnmsfddddddddd" + "checked");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
            }

        }).check();

        //Line Login Api Integration
        LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(getApplicationContext(), CHANNEL_ID);
        lineApiClient = apiClientBuilder.build();

        mFont = Typeface.createFromAsset(getAssets(), "robotlight.ttf");

        user_email.setTypeface(mFont);
        user_password.setTypeface(mFont);
        btn_login.setTypeface(mFont);
        btn_Guest.setTypeface(mFont);
        btn_sign_up.setTypeface(mFont);
        forgotpassword.setTypeface(mFont);
        connect_us.setTypeface(mFont);
        or_txt.setTypeface(mFont);


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        //    System.out.println("sdfffffffffffffffff 1" + isLoggedIn);

        line_login = (ImageView) findViewById(R.id.line_login);
        line_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // App-to-app login
                    Intent loginIntent = LineLoginApi.getLoginIntent(view.getContext(), "1590284140");
                    startActivityForResult(loginIntent, REQUEST_CODE);
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                }


            }
        });
        facebook_login = (LoginButton) findViewById(R.id.facebook_login);

        facebook_login.setReadPermissions(Arrays.asList(EMAIL));
//                facebook_login.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email"));
//                    }
//                });
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Facebook Callback registration
        callbackManager = CallbackManager.Factory.create();
        facebook_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // Log.d("facebook","" + loginResult.getAccessToken().getToken());
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                Log.d("LOGGED", "" + isLoggedIn);

                if (AccessToken.getCurrentAccessToken() == null) {
                    return; // already logged out
                }

                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {

                        LoginManager.getInstance().logOut();

                    }
                }).executeAsync();

                requestUserProfile(loginResult);

                //handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                System.out.println("sdfffffffffffffffff2" + exception.toString());
            }
        });

        SharedPreferences pref1 = ac.getSharedPreferences("LOGIN_INFO", 0); // 0 - for private mode
        is_logged_in = pref1.getBoolean("IsLoggedIn", false);
        rememeber_me = pref1.getBoolean("REMEMBERME", false);


        if (is_logged_in) {
            if (rememeber_me) {
                SharedPreferences pref = ac.getSharedPreferences("LOGIN_INFO", 0);
                user_email.setText(pref.getString("EMAIL", "0"));
                user_password.setText(pref.getString("PASSWORD", "0"));
                user_email.setSelection(user_email.getText().length());
                user_password.setSelection(user_password.getText().length());

            }
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String email = user_email.getText().toString();
//                String password = user_password.getText().toString();
//                if (!validateEmail(email)) {
//                    snakeBaar.showSnackBar(ac, "Not a valid email address!", login_Layout);
//
//                } else if (!validatePassword(password)) {
//
//                    snakeBaar.showSnackBar(ac, "Not a vaild password!", login_Layout);
//
//                }
//                else {
//                    final Dialog disl = new Dialog(ac);
//                    disl.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    disl.setContentView(R.layout.choose_language);
//                    disl.setCancelable(false);
//
//                    final CheckBox check_box_english = (CheckBox) disl.findViewById(R.id.check_box_english);
//                    final CheckBox check_box_cheinese = (CheckBox) disl.findViewById(R.id.check_box_cheinese);
//                    Button submit_btn = (Button) disl.findViewById(R.id.submit_btn);
//
//                    check_box_english.setChecked(true);
//                    check_box_english.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            check_box_english.setChecked(true);
//                            if (check_box_cheinese.isChecked()) {
//                                check_box_cheinese.setChecked(false);
//                            }
//
//                        }
//                    });
//                    check_box_cheinese.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            check_box_cheinese.setChecked(true);
//                            if (check_box_english.isChecked()) {
//                                check_box_english.setChecked(false);
//                            }
//                        }
//                    });
//                    submit_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (check_box_english.isChecked()) {
//                                disl.dismiss();

                doLogin();

//                            } else if (check_box_cheinese.isChecked()) {
//                                Toast.makeText(ac, "work is in progress", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                   disl.show();
//
//
//                }
            }
        });

        btn_Guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
                tinydb.putString("LOGIN","guest_login");
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Signup.class);
                startActivity(i);
            }
        });

        System.out.print("dfreeeeeeeee3dddde3de3"+" "+token +"WELL ");


    }

    public void requestUserProfile(LoginResult loginResult) {
//
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
//
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                      //  MainApplication.hideProg(ac);
//                        accessToken = AccessToken.getCurrentAccessToken();
//                        GraphRequest request = GraphRequest.newMeRequest(
//                                accessToken,
//                                new GraphRequest.GraphJSONObjectCallback() {
//                                    @Override
//                                    public void onCompleted(JSONObject object, GraphResponse response) {
//                                        // Insert your code here
//
//                                        try {
//                                            System.out.println("gdcsgsgsgg   "+response.getJSONObject());
//                                          //  String image = "http://graph.facebook.com/"+response.getJSONObject().getString("id")+"/picture?type=large";
//                                          //  socialMediaLogin(response.getJSONObject().getString("id"), response.getJSONObject().getString("name"), response.getJSONObject().getString("email"), image);
//                                            Log.d("MAIL",""+response.getJSONObject().getString("email"));
//
////                                    session.createLoginSession(response.getJSONObject().getString("name"),response.getJSONObject().getString("email"),response.getJSONObject().getInt("id"),"",false,"");
////                                    System.out.println("fb_Id"+response.getJSONObject().getString("id")+"fbname"+response.getJSONObject().getString("name")+"email"+response.getJSONObject().getString("email"));
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                });
//
//                        Bundle parameters = new Bundle();
//                        parameters.putString("fields", "id,name,email");
//                        request.setParameters(parameters);
//                        request.executeAsync();
////                        Session s = new Session(this);
////                        Session.setActiveSession(s);
////                        Session.OpenRequest request = new Session.OpenRequest(this);
////                        request.setPermissions(Arrays.asList("basic_info","email"));
////                        request.setCallback( /* your callback here */ );
////                        s.openForRead(request);
//
//                    }
//
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//
//    }

        GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                        } else {

                            try {
                                System.out.println("gdcsgsgsgg   " + response.getJSONObject());
                                String image = "http://graph.facebook.com/" + response.getJSONObject().getString("id") + "/picture?type=large";
                                //socialMediaLogin(response.getJSONObject().getString("id"), response.getJSONObject().getString("name"), response.getJSONObject().getString("email"), image);
//                                    session.createLoginSession(response.getJSONObject().getString("name"),response.getJSONObject().getString("email"),response.getJSONObject().getInt("id"),"",false,"");
//                                    System.out.println("fb_Id"+response.getJSONObject().getString("id")+"fbname"+response.getJSONObject().getString("name")+"email"+response.getJSONObject().getString("email"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                Log.e("email", response.getJSONObject().getString("email"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                social_email = response.getJSONObject().getString("email");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                social_username = response.getJSONObject().getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                social_id = response.getJSONObject().getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            social_profile = " ";

                            retrofitSocial_Login();
                            String id = me.optString("id");
                            // send email and id to your web server
                            Log.e("Result1", response.getRawResponse());
                            Log.e("Result2", me.toString());
                            Log.e("id", id);

                        }


                    }
                }).executeAsync();

    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > 2;
    }


    public void doLogin() {
        if (IsInternetOnGlobal.isInternetOn(ac)) {

            retrofitLogin();

        } else {

            snakeBaar.showSnackBar(ac, "No internet connection", login_Layout);

        }
    }

    private void popup() throws JSONException {


        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_forget_password, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirm = (EditText) confirmDialog.findViewById(R.id.editTextOtp);
        TextView titlepop = (TextView) confirmDialog.findViewById(R.id.titlepop);
        LinearLayout poprootll = (LinearLayout) confirmDialog.findViewById(R.id.poprootll);
        titlepop.setText("Forgot Password");


        //Creating an alertdialog builder
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);


        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Displaying the alert dialog
        alertDialog.show();


        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                //Displaying a progressbar
                // final ProgressDialog loading = ProgressDialog.show(SignUp.this, "Authenticating", "Please wait while we check the entered code", false,false);

                //Getting the user entered otp from edittext
                final String forgot_Email = editTextConfirm.getText().toString().trim();
                if (!validateEmail(forgot_Email)) {
//                    Toast.makeText(Login.this,"works",Toast.LENGTH_SHORT).show();
                    editTextConfirm.setError("Please enter a valid email address.");
//                } else if (forgot_Email.isEmpty()) {
//                    editTextConfirm.setError("Email address can not be empty.");

                    // snakeBaar.showSnackBar(ac, "Please enter a valid email address.", linearLayout);
                } else {
                    ForgotpasswdRequest request = new ForgotpasswdRequest();
                    request.setEmail(forgot_Email);

                    if (MainApplication.isNetworkAvailable(ac)) {

                        prog.progDialog(ac);

                        MainApplication.getApiService().forgot_method("text/plain", request).enqueue(new Callback<ForgotpasswdResponse>() {
                            @Override
                            public void onResponse(Call<ForgotpasswdResponse> call, Response<ForgotpasswdResponse> response) {
                                prog.hideProg();
                                if (response.isSuccessful()) {

                                    if (response.body().getRespCode().matches("1010 ")) {

                                        snakeBaar.showSnackBar(ac, response.body().getMessage(), login_Layout);
                                    } else {
                                        snakeBaar.showSnackBar(ac, response.body().getMessage(), login_Layout);

                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ForgotpasswdResponse> call, Throwable t) {
                                prog.hideProg();
                                t.printStackTrace();
                            }
                        });

                    } else {
                        snakeBaar.showSnackBar(ac, "NO INTERNET CONNECTION", login_Layout);
                    }
                    alertDialog.dismiss();

                }

            }


        });
    }


    public void retrofitLogin() {

        LoginRequest loginRequest = new LoginRequest();

            //Show Your Progress Dialog
            prog.progDialog(ac);

        loginRequest.setUsername(user_email.getText().toString());
        loginRequest.setPassword(user_password.getText().toString());

        loginRequest.setDevicetoken(token);
        loginRequest.setDevicetype("A");

        MainApplication.getApiService().login_method("text/plain", loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    prog.hideProg();

                    LoginResponse loginRsponse = response.body();
                    if (response.body().getRespCode().matches("1001")) {
                        if (!user_email.getText().toString().equals(loginRsponse.getUser_data().getUserEmail())) {
                            snakeBaar.showSnackBar(ac, "Email does not exist", login_Layout);
                        }
                        Intent main = new Intent(Login.this, MainActivity.class);
                        startActivity(main);
                        tinydb.putString("LOGIN","user_login");
                        tinydb.putInt("user_id", loginRsponse.getUser_data().getId());

                        tinydb.putString("id", String.valueOf(response.body().getUser_data().getId()));
                        userID = loginRsponse.getUser_data().getId();
                        tinydb.putString("user_name", user_email.getText().toString());
                        tinydb.putString("password", user_password.getText().toString());
                        tinydb.putString("profile", loginRsponse.getUser_data().getProfileImage());
                    //    Log.d("LOGINfhvbueihb",loginRsponse.getUser_data().getProfileImage());
                        tinydb.putString("userName", loginRsponse.getUser_data().getUsername());
                        tinydb.putString("userrole", response.body().getUser_data().getUser_role());
                        Log.d("uswqtydqdfg",loginRsponse.getUser_data().getUsername());

                    } else {
                        snakeBaar.showSnackBar(ac, response.body().getMessage(), login_Layout);
                    }

                } else {
                    prog.hideProg();

                    snakeBaar.showSnackBar(ac, response.body().getMessage(), login_Layout);

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                prog.hideProg();

                System.out.println("siyfdo98efruo8ifuifofiuo9f9p" + t);

                //   snakeBaar.showSnackBar(ac, "Something went wrong..", login_Layout);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
//            callbackManager.onActivityResult(RC_SIGN_IN,REQUEST_CODE,data);
//            com.facebook.Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }

        if (requestCode != REQUEST_CODE) {
            Log.e("ERROR", "Unsupported Request");
            return;
        }


        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);

        switch (result.getResponseCode()) {

            case SUCCESS:
                // Login successful

                String accessToken = result.getLineCredential().getAccessToken().getAccessToken();
                String id = result.getLineProfile().getUserId();
                tinydb.putString("LOGIN","user_login");
                Intent transitionIntent = new Intent(this, MainActivity.class);
                transitionIntent.putExtra("line_profile", result.getLineProfile());
                transitionIntent.putExtra("line_credential", result.getLineCredential());
                transitionIntent.putExtra("display_name", result.getLineProfile().getDisplayName());
                transitionIntent.putExtra("status_message", result.getLineProfile().getStatusMessage());
                transitionIntent.putExtra("user_id", result.getLineProfile().getUserId());
                //transitionIntent.putExtra("user_image",result.getLineProfile().getPictureUrl());

                //Toast.makeText(Login.this, "" + id + accessToken, Toast.LENGTH_SHORT).show();

                social_email = " ";
                social_username = String.valueOf(result.getLineProfile().getDisplayName());
                social_id = String.valueOf(result.getLineProfile().getUserId());
                social_profile = " ";

                retrofitSocial_Login();

//                transitionIntent.putExtra("picture_url", result.getLineProfile().getPictureUrl().toString());
                startActivity(transitionIntent);

                //  AccessToken accessToken1 = result.getLineCredential().getAccessToken();
                break;

            case CANCEL:
                // Login canceled by user
                Log.e("ERROR", "LINE Login Canceled by user!!");
                break;

            default:
                // Login canceled due to other error
                Log.e("ERROR", "Login FAILED!");
                Log.e("ERROR", result.getErrorData().toString());
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    void handleLineSignInResult(LineLoginResult result) {
//        if(result.isSuccess()){
//
//
//        }
//    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            System.out.println(acct.getDisplayName() + "gdashghggghhggfad" + acct.getEmail());


            social_email = acct.getEmail();
            social_username = acct.getDisplayName();
            social_id = acct.getId();
            social_profile = String.valueOf(acct.getPhotoUrl());

            retrofitSocial_Login();


        } else {
            // Signed out, show unauthenticated UI.

        }
    }


    @OnClick(R.id.forgotpassword)
    void forgotPassword() {
        try {
            popup();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imgGooglePlusSignIn)
    void onClick() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        mGoogleApiClient.stopAutoManage(Login.this);
//        mGoogleApiClient.disconnect();
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.stopAutoManage(this);
//            mGoogleApiClient.disconnect();
//        }
//    }

    public void retrofitSocial_Login() {
        SocialLoginRequest socialLoginRequest = new SocialLoginRequest();
        prog.progDialog(ac);
        socialLoginRequest.setUsername(social_username);
        socialLoginRequest.setSocialid(social_id);
        socialLoginRequest.setEmail(social_email);
        socialLoginRequest.setPicture(social_profile);

        MainApplication.getApiService().social_login("text/plain", socialLoginRequest).enqueue(new Callback<SocialLoginResponse>() {
            @Override
            public void onResponse(Call<SocialLoginResponse> call, Response<SocialLoginResponse> response) {

                if (response.isSuccessful()) {
                    prog.hideProg();

                    SocialLoginResponse loginRsponse = response.body();
                    if (response.body().getRespCode().matches("1001")) {
                        snakeBaar.showSnackBar(ac, response.body().getMessage(), login_Layout);
                        tinydb.putString("LOGIN","user_login");
                        tinydb.putInt("user_id", response.body().getUserData().getId());
                        tinydb.putString("id", String.valueOf(response.body().getUserData().getId()));
                        tinydb.putString("userName", response.body().getUserData().getUsername());
                        tinydb.putString("profileImage", response.body().getUserData().getProfileImage());
                        Intent login = new Intent(ac, MainActivity.class);
                        startActivity(login);

                    } else {
                        snakeBaar.showSnackBar(ac, response.body().getMessage(), login_Layout);
                    }

                } else {
                    prog.hideProg();
                    snakeBaar.showSnackBar(ac, response.body().getMessage(), login_Layout);
                }
            }

            @Override
            public void onFailure(Call<SocialLoginResponse> call, Throwable t) {

                prog.hideProg();
                snakeBaar.showSnackBar(ac, "Something went wrong..", login_Layout);

            }
        });
    }

    @Override
    public void onBackPressed() {

        finishAffinity();
        finish();
        super.onBackPressed();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//
//    }

}