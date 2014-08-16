package com.diacht.simpleusers.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.ui.activity.BaseActivity;
import com.diacht.simpleusers.utils.InputFormException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Login/Authorization Fragment
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

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.log_btn)
    public void onLogin() {
        try {
            InputFormException.assertBlankEditText(mLogin, R.string.error_login);
            InputFormException.assertBlankEditText(mPassword, R.string.error_pass);
            InputFormException.assertBlankEditText(mName, R.string.error_name);
            InputFormException.assertEmailValid(mEmail, R.string.error_email);
            InputFormException.assertTrue(mPassword.getText().toString().equals(
                            mPasswordConfirm.getText().toString()),
                    R.string.error_valid_password);
            //TODO error Latitude,Logitude
            if(mLatitude.getEditableText().toString().trim().length() == 0 ||
                    mLogitude.getEditableText().toString().trim().length() == 0){
                ((BaseActivity)getActivity()).ErrorCoordinatesDialog(R.string.error_coordinates, this);
            }else {
                setNewData();
            }
        } catch (InputFormException e) {
            ((BaseActivity)getActivity()).ErrorDialog(e.getMessageResource());
        }
    }

    private void setNewData() {
    }

    @Override
    public void setDialogChoice() {
        setNewData();
    }
}
