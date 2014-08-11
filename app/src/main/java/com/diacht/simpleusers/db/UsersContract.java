package com.diacht.simpleusers.db;

import android.net.Uri;

import com.diacht.simpleusers.dao.User;
import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.Column.Type;
import com.tjeannin.provigen.annotation.ContentUri;

/**
 * UsersContract
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public interface UsersContract extends ProviGenBaseContract {
    @Column(Type.INTEGER)
    public static final String innerId = User.FIELD_ID;
    @Column(Type.TEXT)
    public static final String name = User.FIELD_NAME;
    @Column(Type.TEXT)
    public static final String email = User.FIELD_EMAIL;
    @Column(Type.REAL)
    public static final String longitude = User.FIELD_LONGITUDE;
    @Column(Type.REAL)
    public static final String latitude = User.FIELD_LATITUDE;
    @Column(Type.TEXT)
    public static final String avatar = User.FIELD_AVATAR;
    @Column(Type.TEXT)
    public static final String www = User.FIELD_WWW;
    @Column(Type.TEXT)
    public static final String phone = User.FIELD_PHONE;


    @ContentUri
    public static final Uri CONTENT_URI = Uri.parse("content://com.diacht.simpleusers/users");
}
