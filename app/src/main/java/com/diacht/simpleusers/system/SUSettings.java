package com.diacht.simpleusers.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SUSettings
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class SUSettings{
    private final static String PREF_SELECT_CATEGORY = "select_category";
    private SharedPreferences mSettings;

    public SUSettings(Context context) {
        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        loadSettings();
    }

     protected void loadSettings() {
//        mSelectCategory = mSettings.getInt(PREF_SELECT_CATEGORY, 0);
    }

//    public int getSelectCategory() {
//        return mSelectCategory;
//    }
//
//    public void setSelectCategory(int mSelectCategory) {
//        this.mSelectCategory = mSelectCategory;
//        mSettings.edit().putInt(PREF_SELECT_CATEGORY, mSelectCategory).commit();
//    }
}
