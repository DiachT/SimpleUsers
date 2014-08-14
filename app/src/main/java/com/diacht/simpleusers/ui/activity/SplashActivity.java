package com.diacht.simpleusers.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.diacht.simpleusers.R;

/**
 * Splash screen activity.
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class SplashActivity extends ActionBarActivity {
    private static final long TIMEOUT_DELAY = 2000;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mText = (TextView) findViewById(R.id.splash_text);
        startMainActivity();
    }

    @Override
    protected void onResume() {
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        super.onResume();
    }

    private void startMainActivity() {
        mText.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, TIMEOUT_DELAY);
    }
}
