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
    public static final int NO_COORDINATES = 10000;

    public static final String FIELD_NAME = "name";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_AVATAR = "avatar";
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String FIELD_LATITUDE = "latitude";
    public static final String FIELD_WWW = "www";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_LOGIN = "login";

    private String mName;
    private String mEmail;
    private String mAvatar;
    private double mLongitude;
    private double mLatitude;
    private String mWww;
    private String mPhone;
    private String mPassword;
    private String mLogin;

    public User(String name, String email, String avatar, double longitude,
                double latitude, String www, String phone, String password, String login) {
        setName(name);
        setEmail(email);
        setAvatar(avatar);
        setLatitude(latitude);
        setLongitude(longitude);
        setWww(www);
        setPhone(phone);
        setPassword(password);
        setLogin(login);
    }

    public User() {}

    public ContentValues toContentValues() {
        ContentValues result = new ContentValues();
        result.put(UsersContract.name, mName);
        result.put(UsersContract.email, mEmail);
        result.put(UsersContract.avatar, mAvatar);
        result.put(UsersContract.latitude, mLatitude);
        result.put(UsersContract.longitude, mLongitude);
        result.put(UsersContract.phone, mPhone);
        result.put(UsersContract.www, mWww);
        result.put(UsersContract.password, mPassword);
        result.put(UsersContract.login, mLogin);
        return result;
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

    public String getWww() {
        return mWww;
    }

    public void setWww(String mWww) {
        this.mWww = mWww;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String mLogin) {
        this.mLogin = mLogin;
    }
}
