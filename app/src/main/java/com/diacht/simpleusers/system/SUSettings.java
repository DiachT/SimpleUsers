package com.diacht.simpleusers.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SUSettings
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class SUSettings{
    private final static String PREF_PASSWORD = "PREF_PASSWORD";
    private final static String PREF_LOGIN = "PREF_LOGIN";
    private final static String PREF_IS_LOGIN = "PREF_IS_LOGIN";
    private SharedPreferences mSettings;
    private String mPassword;
    private String mLogin;
    private boolean mIsLogin;

    public SUSettings(Context context) {
        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        loadSettings();
    }

    protected void loadSettings() {
        mPassword = mSettings.getString(PREF_PASSWORD, "");
        mLogin = mSettings.getString(PREF_LOGIN, "");
        mIsLogin = mSettings.getBoolean(PREF_IS_LOGIN, false);
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
        mSettings.edit().putString(PREF_PASSWORD, mPassword).commit();
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String mLogin) {
        this.mLogin = mLogin;
        mSettings.edit().putString(PREF_LOGIN, mLogin).commit();
    }

    public boolean getIsLogin() {
        return mIsLogin;
    }

    public void setIsLogin(boolean mIsLogin) {
        this.mIsLogin = mIsLogin;
        mSettings.edit().putBoolean(PREF_IS_LOGIN, mIsLogin).commit();
    }
}
