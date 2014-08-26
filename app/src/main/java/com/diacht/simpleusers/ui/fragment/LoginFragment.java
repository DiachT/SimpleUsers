package com.diacht.simpleusers.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.dao.User;
import com.diacht.simpleusers.db.UsersContract;
import com.diacht.simpleusers.system.Utils;
import com.diacht.simpleusers.ui.activity.BaseActivity;
import com.diacht.simpleusers.ui.activity.MainActivity;
import com.diacht.simpleusers.ui.activity.TwitterActivity;
import com.diacht.simpleusers.utils.InputFormException;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Login/Registration Fragment
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class LoginFragment extends BaseFragment implements BaseActivity.OnSetDialogChoiceListener {
    @InjectView(R.id.log_login)
    protected EditText mLogin;
    @InjectView(R.id.log_password)
    protected EditText mPassword;
    @InjectView(R.id.log_password_confirm)
    protected EditText mPasswordConfirm;
    @InjectView(R.id.log_name)
    protected EditText mName;
    @InjectView(R.id.log_email)
    protected EditText mEmail;
    @InjectView(R.id.log_logitude)
    protected EditText mLogitude;
    @InjectView(R.id.log_latitude)
    protected EditText mLatitude;
    @InjectView(R.id.log_www)
    protected EditText mWww;
    @InjectView(R.id.log_phone)
    protected EditText mPhone;
    @InjectView(R.id.btn_registration)
    protected Button mRegistration;
    private boolean mIsLogin;

    private boolean mIsAddCoordinates = false;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.inject(this, view);
        mIsLogin = getActivity().getContentResolver().query(UsersContract.CONTENT_URI, null,
                null, null, null).moveToFirst();
        setIsLogin();
        mPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mIsLogin && (event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onOk();
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    private void setIsLogin(){
        if(mIsLogin){
            mPasswordConfirm.setVisibility(View.GONE);
            mName.setVisibility(View.GONE);
            mEmail.setVisibility(View.GONE);
            mLogitude.setVisibility(View.GONE);
            mLatitude.setVisibility(View.GONE);
            mWww.setVisibility(View.GONE);
            mPhone.setVisibility(View.GONE);
            mRegistration.setText(R.string.registration);
            setTitle(R.string.login);
        }else{
            mPasswordConfirm.setVisibility(View.VISIBLE);
            mName.setVisibility(View.VISIBLE);
            mEmail.setVisibility(View.VISIBLE);
            mLogitude.setVisibility(View.VISIBLE);
            mLatitude.setVisibility(View.VISIBLE);
            mWww.setVisibility(View.VISIBLE);
            mPhone.setVisibility(View.VISIBLE);
            mRegistration.setText(R.string.login);
            setTitle(R.string.registration);
        }
    }

    @OnClick(R.id.btn_registration)
    public void onLogin() {
        mIsLogin = !mIsLogin;
        setIsLogin();
    }

    @OnClick(R.id.btn_vk)
    public void onLoginVk() {
        actionVk();
    }

    @OnClick(R.id.btn_tw)
    public void onLoginTw() {
        startActivity(new Intent(getActivity(), TwitterActivity.class));
    }

    @OnClick(R.id.log_btn)
    public void onOk() {
        try {
            InputFormException.assertBlankEditText(mLogin, R.string.error_login);
            InputFormException.assertBlankEditText(mPassword, R.string.error_pass);
            if (mIsLogin) {
                Cursor cursor = getActivity().getContentResolver().query(UsersContract.CONTENT_URI, null,
                        User.FIELD_LOGIN + "=? AND " + User.FIELD_PASSWORD + " =? ",
                        new String[]{mLogin.getText().toString(), mPassword.getText().toString()},
                        null);
                if(cursor.moveToFirst()) {
                    mSettings.setId(Utils.getIntFromCursor(cursor, UsersContract._ID));
                    Toast.makeText(getActivity(), R.string.ok_login, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                    cursor.close();
                }else{
                    ((BaseActivity) getActivity()).ErrorDialog(R.string.error_login_pass);
                    cursor.close();
                    return;
                }
            } else {
                InputFormException.assertBlankEditText(mName, R.string.error_name);
                InputFormException.assertEmailValid(mEmail, R.string.error_email);
                InputFormException.assertTrue(mPassword.getText().toString().equals(
                                mPasswordConfirm.getText().toString()),
                        R.string.error_valid_password);
                if (mLatitude.getEditableText().toString().trim().length() == 0 ||
                        mLogitude.getEditableText().toString().trim().length() == 0) {
                    ((BaseActivity) getActivity()).ErrorCoordinatesDialog(R.string.error_coordinates, this, false);
                } else {
                    InputFormException.assertTrue(Double.parseDouble(mLatitude.getText().toString()) < 85 &&
                                    Double.parseDouble(mLatitude.getText().toString()) > -85,
                            R.string.error_latitude);
                    InputFormException.assertTrue(Double.parseDouble(mLogitude.getText().toString()) < 180 &&
                                    Double.parseDouble(mLogitude.getText().toString()) > -180,
                            R.string.error_logitude);
                    mIsAddCoordinates = true;
                    setNewData();
                }
            }
        }
        catch(InputFormException e){
            ((BaseActivity) getActivity()).ErrorDialog(e.getMessageResource());
        }
    }

    private void setNewData() {
        Cursor cursor = getActivity().getContentResolver().query(UsersContract.CONTENT_URI, null,
                User.FIELD_LOGIN + "=? ",
                new String[]{mLogin.getText().toString()},
                null);
        if(cursor.moveToFirst()){
            ((BaseActivity) getActivity()).ErrorDialog(R.string.error_login_exist);
        }else {
            User user = new User(mName.getText().toString(), mEmail.getText().toString(), "",
                    mIsAddCoordinates ? Double.parseDouble(
                            mLogitude.getEditableText().toString()) : User.NO_COORDINATES,
                    mIsAddCoordinates ? Double.parseDouble(
                            mLatitude.getEditableText().toString()) : User.NO_COORDINATES,
                    mWww.getText().toString(), mPhone.getText().toString(), mPassword.getText().toString(),
                    mLogin.getText().toString());
            mSettings.setId(Integer.valueOf(getActivity().getContentResolver().
                    insert(UsersContract.CONTENT_URI, user.toContentValues()).getLastPathSegment()));
            Toast.makeText(getActivity(), R.string.ok_registration, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
        cursor.close();
    }

    @Override
    public void setDialogChoice(boolean data) {
        setNewData();
    }

    protected void actionVk(){
        VKUIHelper.onCreate(getActivity());
        VKSdk.initialize(((BaseActivity) getActivity()).sdkListener, "4515215");
        if (!VKSdk.isLoggedIn()) {
//            ((BaseActivity)getActivity()).setSharingVkText(getSharingText());
            VKSdk.authorize(BaseActivity.sMyScope, true, false);
//                VKSdk.logout();
        }else {
            if (VKSdk.wakeUpSession()) {
                Toast.makeText(getActivity(), R.string.ok_login, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        }
    }
}
