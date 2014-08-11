package com.diacht.simpleusers.dao;

import android.content.ContentValues;

import com.diacht.simpleusers.db.UsersContract;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Dao class for User
 * @author Tetiana Diachuk (diacht@gmail.com)
 *
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_AVATAR = "avatar";
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String FIELD_LATITUDE = "latitude";

    @SerializedName(FIELD_ID)
    private int mId;
    @SerializedName(FIELD_NAME)
    private String mName;
    @SerializedName(FIELD_EMAIL)
    private String mEmail;
    @SerializedName(FIELD_AVATAR)
    private String mAvatar;
    @SerializedName(FIELD_LONGITUDE)
    private double mLongitude;
    @SerializedName(FIELD_LATITUDE)
    private double mLatitude;


    public User(int id, String name, String email, String avatar, double longitude,
                double latitude) {
        setId(id);
        setName(name);
        setEmail(email);
        setAvatar(avatar);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public User() {}

    public ContentValues toContentValues() {
        ContentValues result = new ContentValues();
        result.put(UsersContract.innerId, mId);
        result.put(UsersContract.name, mName);
        result.put(UsersContract.email, mEmail);
        result.put(UsersContract.avatar, mAvatar);
        result.put(UsersContract.latitude, mLatitude);
        result.put(UsersContract.longitude, mLongitude);
        return result;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }
}
