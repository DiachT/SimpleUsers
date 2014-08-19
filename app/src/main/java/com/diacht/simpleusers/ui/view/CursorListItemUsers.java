package com.diacht.simpleusers.ui.view;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.dao.User;
import com.diacht.simpleusers.system.Utils;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * View for one user.
 * @author Tetiana Diachuk (diacht@gmail.com)
 */

public class CursorListItemUsers extends LinearLayout {
    @InjectView(R.id.user_login)
    protected TextView mLogin;
    @InjectView(R.id.user_name)
    protected TextView mName;
    @InjectView(R.id.user_email)
    protected TextView mEmail;
    @InjectView(R.id.user_logitude)
    protected TextView mLongitude;
    @InjectView(R.id.user_latitude)
    protected TextView mLatitude;
    @InjectView(R.id.user_www)
    protected TextView mWww;
    @InjectView(R.id.user_phone)
    protected TextView mPhone;
    @InjectView(R.id.user_foto)
    protected ImageView mFoto;
    private String mUserEmail;

    public CursorListItemUsers(final Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.list_item_user, this);
        ButterKnife.inject(this);
    }

    private void setDataToTextView(Cursor cursor, TextView view, String field, int text) {
        String str = Utils.getStringFromCursor(cursor, field);
        if (str.length() > 0) {
            view.setVisibility(View.VISIBLE);
            view.setText(getContext().getText(text) + ": " + str);
        } else {
            view.setVisibility(View.GONE);
    }
    }

    public void setData(Cursor cursor) {
        setDataToTextView(cursor, mLogin, User.FIELD_LOGIN, R.string.hint_login);
        if(Utils.getDoubleFromCursor(cursor, User.FIELD_LATITUDE) != User.NO_COORDINATES &&
                Utils.getDoubleFromCursor(cursor, User.FIELD_LONGITUDE) != User.NO_COORDINATES) {
            setDataToTextView(cursor, mLatitude, User.FIELD_LATITUDE, R.string.hint_latitude);
            setDataToTextView(cursor, mLongitude, User.FIELD_LONGITUDE, R.string.hint_logitude);
        }else{
            mLatitude.setVisibility(View.GONE);
            mLongitude.setVisibility(View.GONE);
        }
        setDataToTextView(cursor, mName, User.FIELD_NAME, R.string.hint_name);
        setDataToTextView(cursor, mEmail, User.FIELD_EMAIL, R.string.hint_email);
        setDataToTextView(cursor, mPhone, User.FIELD_PHONE, R.string.hint_phone);
        setDataToTextView(cursor, mWww, User.FIELD_WWW, R.string.hint_www);
        mUserEmail = Utils.getStringFromCursor(cursor, User.FIELD_EMAIL);
        String imageUrl = Utils.getStringFromCursor(cursor, User.FIELD_AVATAR);
        if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
            Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.no_avatar).into(mFoto);
        }
    }

    public String getUserEmail() {
        return mUserEmail;
    }
}
