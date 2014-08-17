package com.diacht.simpleusers.ui.activity;

import android.os.Bundle;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.ui.fragment.UsersFragment;

/**
 * MainActivity
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            startFragment(UsersFragment.newInstance(), true);
        }
    }
}