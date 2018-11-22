package com.example.mandeep.moviebooking1;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static SharedPref mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "userinfo";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USER_PHONE = "userphone";
    private static final String KEY_USER_GENDER = "usergender";
    private static final String KEY_USER_PASS = "userpass";

    private SharedPref(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPref getmIntances(Context context) {
        if(mInstance == null) {
            mInstance = new SharedPref(context);
        }
        return  mInstance;
    }

    public boolean userLogin(int id, String username, String email, String phone, String gender) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putString(KEY_USER_GENDER, gender);
        editor.apply();
        return true;
    }

//    public boolean isLoggedIn() {
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        if(sharedPreferences.getString(KEY_USER_EMAIL, null) != null) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean logout() {
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//        return true;
//
//    }

    public String getUserName () {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getUserPhone () {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_PHONE, null);
    }

    public String getUserEmail () {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public Integer getUSerID () {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID, 0);
    }

    public String getUserGender () {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_GENDER, null);
    }

}

