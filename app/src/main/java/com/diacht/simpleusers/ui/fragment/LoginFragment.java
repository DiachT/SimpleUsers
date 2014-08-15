package com.diacht.simpleusers.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.diacht.simpleusers.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Login/Authorization Fragment
 * @author Tetiana Diachuk (diacht@gmail.com) on 14-Aug-14.
 */
public class LoginFragment extends BaseFragment{
    @InjectView(R.id.log_login)
    protected EditText mLogin;

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
    }
}
