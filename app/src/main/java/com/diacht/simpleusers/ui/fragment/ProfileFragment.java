package com.diacht.simpleusers.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
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

    private boolean mIsAddCoordinates = false;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
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
            String imagePath = cursor.getString(column_index_data);
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
//            InputFormException.assertBlankEditText(mLogin, R.string.error_login);
            InputFormException.assertBlankEditText(mPassword, R.string.error_pass);
//            if (mIsLogin) {
//                Cursor cursor = getActivity().getContentResolver().query(UsersContract.CONTENT_URI, null,
//                        User.FIELD_LOGIN + "=? AND " + User.FIELD_PASSWORD + " =? ",
//                        new String[]{mLogin.getText().toString(), mPassword.getText().toString()},
//                        null);
//                if(cursor.moveToFirst()) {
//                    mSettings.setId(Utils.getIntFromCursor(cursor, UsersContract._ID));
//                    Toast.makeText(getActivity(), R.string.ok_login, Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                    getActivity().finish();
//                }
//            } else {
//                InputFormException.assertBlankEditText(mName, R.string.error_name);
//                InputFormException.assertEmailValid(mEmail, R.string.error_email);
//                InputFormException.assertTrue(mPassword.getText().toString().equals(
//                                mPasswordConfirm.getText().toString()),
//                        R.string.error_valid_password);
//                //TODO error Latitude,Logitude
//                if (mLatitude.getEditableText().toString().trim().length() == 0 ||
//                        mLogitude.getEditableText().toString().trim().length() == 0) {
//                    ((BaseActivity) getActivity()).ErrorCoordinatesDialog(R.string.error_coordinates, this);
//                } else {
//                    mIsAddCoordinates = true;
//                    setNewData();
//                }
//            }
        }
        catch(InputFormException e){
            ((BaseActivity) getActivity()).ErrorDialog(e.getMessageResource());
        }
    }

    private void setNewData() {
//        User user = new User(mName.getText().toString(), mEmail.getText().toString(),"",
//                mIsAddCoordinates ? Double.parseDouble(
//                        mLogitude.getEditableText().toString()) : User.NO_COORDINATES,
//                mIsAddCoordinates ? Double.parseDouble(
//                        mLatitude.getEditableText().toString()) : User.NO_COORDINATES,
//                mWww.getText().toString(), mPhone.getText().toString(), mPassword.getText().toString(),
//                mLogin.getText().toString());
//        mSettings.setId(Integer.valueOf(getActivity().getContentResolver().
//                insert(UsersContract.CONTENT_URI, user.toContentValues()).getLastPathSegment()));
//        Toast.makeText(getActivity(), R.string.ok_registration, Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(getActivity(), MainActivity.class));
//        getActivity().finish();
    }

    @Override
    public void setDialogChoice() {
        setNewData();
    }
}
