package com.diacht.simpleusers.ui.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.dao.User;
import com.diacht.simpleusers.db.UsersContract;
import com.diacht.simpleusers.system.Utils;
import com.diacht.simpleusers.ui.activity.BaseActivity;
import com.diacht.simpleusers.ui.activity.LoginActivity;
import com.diacht.simpleusers.utils.InputFormException;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKSdk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Profile edit Fragment
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class ProfileFragment extends BaseFragment implements BaseActivity.OnSetDialogChoiceListener {
    private static final int TAKE_PICTURE = 1234;
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
    @InjectView(R.id.prof_no_login)
    protected EditText mProfNoLogin;
    @InjectView(R.id.prof_foto)
    protected ImageView mFoto;
    private Cursor mCursor;
    private ContentObserver mObserver;
    private boolean mIsAddCoordinates = false;
    private String mAvatar;
    private String mLoginText;
    private String mNewFotoPath;

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
        mLoginText = Utils.getStringFromCursor(mCursor, User.FIELD_LOGIN);
        if(TextUtils.isEmpty(mLoginText)){
            mProfNoLogin.setVisibility(View.VISIBLE);
            mLogin.setVisibility(View.GONE);
        }else {
            mProfNoLogin.setVisibility(View.GONE);
            mLogin.setVisibility(View.VISIBLE);
            mLogin.setText(mLoginText);
        }
        mEmail.setText(Utils.getStringFromCursor(mCursor, User.FIELD_EMAIL));
        mPhone.setText(Utils.getStringFromCursor(mCursor, User.FIELD_PHONE));
        mWww.setText(Utils.getStringFromCursor(mCursor, User.FIELD_WWW));
        mAvatar = Utils.getStringFromCursor(mCursor, User.FIELD_AVATAR);
        if(Utils.getDoubleFromCursor(mCursor, User.FIELD_LATITUDE) != User.NO_COORDINATES &&
                Utils.getDoubleFromCursor(mCursor, User.FIELD_LONGITUDE) != User.NO_COORDINATES) {
            mCoordinates.setText(Utils.getStringFromCursor(mCursor, User.FIELD_LATITUDE) + ", " +
                    Utils.getStringFromCursor(mCursor, User.FIELD_LONGITUDE ));
            mIsAddCoordinates = true;
        }else{
            mIsAddCoordinates = false;
        }
        if (mAvatar != null && !TextUtils.isEmpty(mAvatar)) {
            Picasso.with(getActivity()).load(mAvatar).placeholder(R.drawable.no_avatar).into(mFoto);
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
        inflater.inflate(R.menu.profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_users) {
            getFragmentManager().popBackStack();
            return true;
        } else
        if (item.getItemId() == R.id.action_logout) {
            try{
                VKSdk.logout();
            }catch (Exception e){}
            ((BaseActivity) getActivity()).disconnectTwitter();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            Toast.makeText(getActivity(), R.string.action_logout, Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return true;
        }
        return false;
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                Bundle extras = resultData.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                File filesDir = getActivity().getExternalFilesDir("foto");
                File foto = new File(filesDir, mLoginText + (new Date()).getTime() + ".jpg");
                Uri fileUri = Uri.fromFile(foto);
                mNewFotoPath = fileUri.toString();
                FileOutputStream out = new FileOutputStream(foto);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 30, out);
                out.close();
                if (mAvatar != null && !TextUtils.isEmpty(mAvatar)) {
                    new File(mAvatar).delete();
                }
                ContentValues result = new ContentValues();
                result.put(UsersContract.avatar, mNewFotoPath);
                getActivity().getContentResolver().update(UsersContract.CONTENT_URI, result,
                        UsersContract._ID + "= ?", new String[]{String.valueOf(mSettings.getId())});
                Picasso.with(getActivity()).load(mNewFotoPath).skipMemoryCache()
                        .placeholder(R.drawable.no_avatar).into(mFoto);
                Toast.makeText(getActivity(), R.string.foto_update, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.prof_coordinates)
    public void onMap() {
        if(mIsAddCoordinates){
            startFragment(MapFragmentCoordinates.newInstance(), true);
        }else{
            ((BaseActivity) getActivity()).ErrorCoordinatesDialog(R.string.add_coordinates, this, true);
        }
    }

    @OnClick(R.id.prof_btn)
    public void onOk() {
        try {
            if(mProfNoLogin.getVisibility() == View.VISIBLE) {
                InputFormException.assertBlankEditText(mProfNoLogin, R.string.error_login);
                InputFormException.assertBlankEditText(mPasswordNew, R.string.error_pass_new);
                Cursor cursor = getActivity().getContentResolver().query(UsersContract.CONTENT_URI, null,
                        User.FIELD_LOGIN + "=? ",
                        new String[]{mProfNoLogin.getText().toString()},
                        null);
                if (cursor.moveToFirst()) {
                    ((BaseActivity) getActivity()).ErrorDialog(R.string.error_login_exist);
                    cursor.close();
                    return;
                }
                cursor.close();
            }
            InputFormException.assertTrue(mPassword.getText().toString().equals(
                            Utils.getStringFromCursor(mCursor, User.FIELD_PASSWORD)),
                    R.string.error_pass);
            InputFormException.assertBlankEditText(mName, R.string.error_name);
            InputFormException.assertEmailValid(mEmail, R.string.error_email);
            InputFormException.assertTrue(mPasswordNew.getText().toString().equals(
                            mPasswordConfirm.getText().toString()),
                    R.string.error_valid_password);
            if(!mIsAddCoordinates){
                ((BaseActivity) getActivity()).ErrorCoordinatesDialog(R.string.error_coordinates, this, false);
            } else {
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
        result.put(UsersContract.phone, mPhone.getText().toString());
        result.put(UsersContract.www, mWww.getText().toString());
        if(mProfNoLogin.getVisibility() == View.VISIBLE) {
            result.put(UsersContract.login, mProfNoLogin.getText().toString());
        }
        if (TextUtils.isEmpty(mPasswordNew.getText().toString())) {
            result.put(UsersContract.password, mPassword.getText().toString());
        }else{
            result.put(UsersContract.password, mPasswordNew.getText().toString());
        }
        getActivity().getContentResolver().update(UsersContract.CONTENT_URI, result,
                UsersContract._ID + "= ?", new String[]{String.valueOf(mSettings.getId())});
        Toast.makeText(getActivity(), R.string.ok_edit_profile, Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(getActivity(), MainActivity.class));
//        getActivity().finish();
    }

    @Override
    public void setDialogChoice(boolean addCoordinates) {
        if (addCoordinates) {
            startFragment(MapFragmentCoordinates.newInstance(), true);
        } else {
            setNewData();
        }
    }
}
