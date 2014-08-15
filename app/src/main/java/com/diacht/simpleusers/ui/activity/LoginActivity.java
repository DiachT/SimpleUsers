package com.diacht.simpleusers.ui.activity;

import android.os.Bundle;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.ui.fragment.LoginFragment;

/**
 * LoginActivity
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class LoginActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            startFragment(LoginFragment.newInstance(), false, false);
        }
    }
}
