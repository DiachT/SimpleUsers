package com.diacht.simpleusers.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.diacht.simpleusers.R;

/**
 * Splash screen activity.
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class SplashActivity extends Activity {
    private static final long TIMEOUT_DELAY = 2500;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mText = (TextView) findViewById(R.id.splash_text);
        startMainActivity();
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
