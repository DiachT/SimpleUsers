package com.diacht.simpleusers.ui.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.dao.User;
import com.diacht.simpleusers.db.UsersContract;
import com.diacht.simpleusers.system.Utils;
import com.diacht.simpleusers.ui.activity.BaseActivity;
import com.diacht.simpleusers.ui.activity.MainActivity;
import com.diacht.simpleusers.utils.InputFormException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Profile edit Fragment
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class ProfileFragment extends BaseFragment implements BaseActivity.OnSetDialogChoiceListener {
    @InjectView(R.id.prof_login)
    protected TextView mLogin;
    @InjectView(R.id.prof_password)
    protected EditText mPassword;
    @InjectView(R.id.prof_password_new)
    protected EditText mPasswordNew;
    @InjectView(R.id.prof_password_confirm)
    protected EditText mPasswordConfirm;
    @InjectView(R.id.prof_name)
    protected EditText mName;
    @InjectView(R.id.prof_email)
    protected EditText mEmail;
    @InjectView(R.id.prof_coordinates)
    protected Button mCoordinates;
    @InjectView(R.id.prof_www)
    protected EditText mWww;
    @InjectView(R.id.prof_phone)
    protected EditText mPhone;
    private Cursor mCursor;
    private ContentObserver mObserver;
    private boolean mIsAddCoordinates = false;
    private String mAvatarURL;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mObserver = new ContentObserver(new Handler()) {
            @SuppressWarnings("deprecation")
            @Override
            public void onChange(boolean selfChange) {
                mCursor.requery();
                updateContentFromCursor();
                super.onChange(selfChange);
            }
        };
    }

    private void updateContentFromCursor() {
        if (mCursor == null || !mCursor.moveToFirst()) {
            return;
        }
        setProfileData();
    }

    private void setProfileData() {
        mName.setText(Utils.getStringFromCursor(mCursor, User.FIELD_NAME));
        mLogin.setText(Utils.getStringFromCursor(mCursor, User.FIELD_LOGIN));
        mEmail.setText(Utils.getStringFromCursor(mCursor, User.FIELD_EMAIL));
        mPhone.setText(Utils.getStringFromCursor(mCursor, User.FIELD_PHONE));
        mWww.setText(Utils.getStringFromCursor(mCursor, User.FIELD_WWW));
        mAvatarURL = Utils.getStringFromCursor(mCursor, User.FIELD_AVATAR);
        if(Utils.getDoubleFromCursor(mCursor, User.FIELD_LATITUDE) != User.NO_COORDINATES &&
                Utils.getDoubleFromCursor(mCursor, User.FIELD_LONGITUDE) != User.NO_COORDINATES) {
            mCoordinates.setText(Utils.getStringFromCursor(mCursor, User.FIELD_LATITUDE) + ", " +
                    Utils.getStringFromCursor(mCursor, User.FIELD_LONGITUDE ));
            mIsAddCoordinates = true;
        }else{
            mIsAddCoordinates = false;
        }
    }

    private void prepareCursor() {
        Cursor cursor = getActivity().getContentResolver().query(
                UsersContract.CONTENT_URI, null, UsersContract._ID + "= ?",
                new String[]{String.valueOf(mSettings.getId())}, null);
        setCursor(cursor);
    }

    private void setCursor(Cursor cursor) {
        if (cursor == null) {
            if (mCursor != null) {
                mCursor.unregisterContentObserver(mObserver);
                mCursor.close();
                mCursor = null;
            }
        } else {
            mCursor = cursor;
            mCursor.registerContentObserver(mObserver);
            updateContentFromCursor();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareCursor();
        setTitle(R.string.users);
    }

    @Override
    public void onPause() {
        setCursor(null);
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        ButterKnife.inject(this, view);
        setTitle(R.string.action_profile);
        return view;
    }

    @OnClick(R.id.prof_foto)
    public void onFoto() {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultData != null) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, null, null, null);
            int column_index_data = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToLast();
            mAvatarURL = cursor.getString(column_index_data);
//            Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
//            imageView.setImageBitmap(bitmapImage );
            cursor.close();
        }
    }

    @OnClick(R.id.prof_coordinates)
    public void onMap() {
        //TODO
        if(true){
            startFragment(MapFragmentCoordinates.newInstance(), true);
        }
    }

    @OnClick(R.id.prof_btn)
    public void onOk() {
        try {
            InputFormException.assertTrue(mPassword.getText().toString().equals(
                            Utils.getStringFromCursor(mCursor, User.FIELD_PASSWORD)),
                    R.string.error_pass);
            InputFormException.assertBlankEditText(mName, R.string.error_name);
            InputFormException.assertEmailValid(mEmail, R.string.error_email);
            InputFormException.assertBlankEditText(mPassword, R.string.error_pass_new);
            InputFormException.assertTrue(mPasswordNew.getText().toString().equals(
                            mPasswordConfirm.getText().toString()),
                    R.string.error_valid_password);
            if(mIsAddCoordinates ||
                    (Utils.getDoubleFromCursor(mCursor, User.FIELD_LATITUDE) != User.NO_COORDINATES &&
                            Utils.getDoubleFromCursor(mCursor, User.FIELD_LONGITUDE) != User.NO_COORDINATES)){
                ((BaseActivity) getActivity()).ErrorCoordinatesDialog(R.string.error_coordinates, this);
            } else {
                mIsAddCoordinates = true;
                setNewData();
            }
        }
        catch(InputFormException e){
            ((BaseActivity) getActivity()).ErrorDialog(e.getMessageResource());
        }
    }

    private void setNewData() {
        ContentValues result = new ContentValues();
        result.put(UsersContract.name, mName.getText().toString());
        result.put(UsersContract.email, mEmail.getText().toString());
        result.put(UsersContract.avatar, mAvatarURL);
//        result.put(UsersContract.latitude, mLatitude);
//        result.put(UsersContract.longitude, mLongitude);
        result.put(UsersContract.phone, mPhone.getText().toString());
        result.put(UsersContract.www, mWww.getText().toString());
        result.put(UsersContract.password, mPasswordNew.getText().toString());
        getActivity().getContentResolver().update(UsersContract.CONTENT_URI, result,
                UsersContract._ID + "= ?", new String[]{String.valueOf(mSettings.getId())});
        Toast.makeText(getActivity(), R.string.ok_edit_profile, Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(getActivity(), MainActivity.class));
//        getActivity().finish();
    }

    @Override
    public void setDialogChoice() {
        setNewData();
    }
}
