package com.diacht.simpleusers.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SUSettings
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class SUSettings{
    private final static String PREF_USER_ID = "PREF_USER_ID";
    public final static int NOT_LOGIN = -100;
    private SharedPreferences mSettings;
    private int mId;

    public SUSettings(Context context) {
        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        loadSettings();
    }

    protected void loadSettings() {
        mId = mSettings.getInt(PREF_USER_ID, NOT_LOGIN);
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
        mSettings.edit().putInt(PREF_USER_ID, mId).commit();
    }
}
