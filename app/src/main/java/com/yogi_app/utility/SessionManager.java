package com.yogi_app.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Android on 4/18/2017.
 */

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN_INFO";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String REMEMBER_ME_CHECK = "REMEMBERME";


    public static final String KEY_NAME = "NAME";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "EMAIL";
    public static final String KEY_ID = "USERID";
    public static final String KEY_PASSWORD = "PASSWORD";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String email, int id, String password){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ID, String.valueOf(id));

        editor.putString(KEY_PASSWORD, password);

        // commit changes
        editor.commit();
    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_ID, pref.getString(KEY_ID , null));

        // return user
        return user;
    }



    public void create_remember_me(boolean value){
        // Storing login value as TRUE

        editor.putBoolean(REMEMBER_ME_CHECK, value);

        editor.commit();
    }
    public void createAboufragment(String name, String email, String id, String phoneno, String appcolor){


        editor.putString("profile_name", name);
        editor.putString("profile_email", email);
        editor.putString("profile_id", id);
        editor.putString("profile_phone", phoneno);
       editor.putString("profile_app_color", appcolor);

        editor.commit();
    }




}
