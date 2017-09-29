package com.wta.NewCloudApp.jiuwei99986.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wta.NewCloudApp.jiuwei99986.App;

/**
 * Created by 小小程序员 on 2016/12/30.
 */

public class SharedPrefrenceManager {
    static final String USER_TOKEN = "token";
    static final String WIDTH = "width";
    static final String NICK_FLAG = "nick_flag";
    static final String SIG = "sig";
    static final String LIVE_NUMBER = "live_number";
    static final String ASK_REFLASH = "ask_reflash";

    private static SharedPreferences mSharedPreferences;
    private static SharedPrefrenceManager mInstance;
    private Context mContext;
    static final String isLogin = "isLogin";
    static final String canRefresh = "canRefresh";

    public static SharedPrefrenceManager getInstance() {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        }
        if (mInstance == null) mInstance = new SharedPrefrenceManager();
        return mInstance;
    }

    public void Initialize(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

    }

    public void setUserId(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public void setDisplayWidth(float width) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(WIDTH, width);
        editor.apply();
    }

    public float getDisplayWidth() {
        return mSharedPreferences.getFloat(WIDTH, 300);
    }

    public void setNick(String nick) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(NICK_FLAG, nick);
        editor.apply();
    }

    public String getNick() {
        return mSharedPreferences.getString(NICK_FLAG, "");
    }

    public String getUserId() {
        return mSharedPreferences.getString(USER_TOKEN, "310002");
    }

    public boolean getCanRefresh() {
        return mSharedPreferences.getBoolean(canRefresh, false);
    }

    public void setCanRefresh(boolean refresh) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(canRefresh, refresh);
        editor.apply();
    }

    public boolean getIsLogin() {
        return mSharedPreferences.getBoolean(isLogin, false);
    }

    public void setIsLogin(boolean login) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(isLogin, login);
        editor.apply();
    }

    public void setSig(String sig) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SIG, sig);
        editor.apply();
    }

    public String getSig() {
        return mSharedPreferences.getString(SIG, "");
    }
    public void setLiveNumber(String number) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LIVE_NUMBER, number);
        editor.apply();
    }

    public String getLiveNumber() {
        return mSharedPreferences.getString(LIVE_NUMBER, "");
    }

    public void setAskCanRedlash(boolean b) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(ASK_REFLASH, b);
        editor.apply();
    }

    public boolean getAskCanRefresh() {
        return mSharedPreferences.getBoolean(ASK_REFLASH, false);
    }
    public void clear(){
        mSharedPreferences.edit().clear().commit();
    }
}
