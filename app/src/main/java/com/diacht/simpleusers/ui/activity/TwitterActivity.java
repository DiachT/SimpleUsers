package com.diacht.simpleusers.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.dao.User;
import com.diacht.simpleusers.db.UsersContract;
import com.diacht.simpleusers.system.SUApplication;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * TwitterActivity
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class TwitterActivity extends Activity {
    private static final String TAG = "simple_users";
    private static Twitter twitter;
    private static  RequestToken requestToken;
    private SharedPreferences mSharedPreferences;
    public static final String EXTRA_MESSAGE_DATA = "EXTRA_MESSAGE_DATA";
    public static final String PREFERENCE_NAME = "twitter_oauth";
    public static final String PREF_KEY_SECRET = "oauth_token_secret";
    public static final String PREF_KEY_TOKEN = "oauth_token";
    public static final String IEXTRA_OAUTH_VERIFIER = "oauth_verifier";
    public static final String CALLBACK_URL = "oauth://simple_users";
    public static final String CONSUMER_KEY = "JpBsQ63zRK3XCePT1bow5sIm9";
    public static final String CONSUMER_SECRET = "6ymVIoGSsJTmOWVkwifbuHKrQowZHTvfeUYH9WeIQ2MwTrVmjJ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        /**
         * Handle OAuth Callback
         */
        Intent intent = getIntent();
        if (isConnected()) {
            Toast.makeText(TwitterActivity.this, R.string.ok_registration, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TwitterActivity.this, MainActivity.class));
            finish();
        } else {
            Uri uri = getIntent().getData();
            if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
                String verif = uri.getQueryParameter(IEXTRA_OAUTH_VERIFIER);
                new GetAccessToken().execute(verif);
            } else {
                new OAuthUrl().execute();
            }
        }

    }

    /**
     * get twitter authtoken and secret
     */
    private class GetAccessToken extends AsyncTask<String, Void, AccessToken> {

        @Override
        protected AccessToken doInBackground(String... voids) {
            try {
                ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
                configurationBuilder.setOAuthConsumerKey(CONSUMER_KEY);
                configurationBuilder.setOAuthConsumerSecret(CONSUMER_SECRET);
                twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, voids[0]);
                return accessToken;
            } catch (Exception e) {
                Log.e(TAG, "error " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(AccessToken accessToken) {
            super.onPostExecute(accessToken);
            if (accessToken != null) {
                Log.d("mylog", accessToken.getToken() + "  " + accessToken.getTokenSecret());
                Editor e = mSharedPreferences.edit();
                e.putString(PREF_KEY_TOKEN, accessToken.getToken());
                e.putString(PREF_KEY_SECRET, accessToken.getTokenSecret());
                e.commit();
                int id = (int) accessToken.getUserId();
                Cursor cursor = getContentResolver().query(UsersContract.CONTENT_URI, null,
                        UsersContract._ID + "=? ",
                        new String[]{String.valueOf(id)},
                        null);
                if (!cursor.moveToFirst()) {
                    ContentValues result = new ContentValues();
                    result.put(UsersContract._ID, id);
                    result.put(UsersContract.password, "");
                    result.put(UsersContract.longitude, User.NO_COORDINATES);
                    result.put(UsersContract.latitude, User.NO_COORDINATES);
                    ((SUApplication) getApplication()).getSettings().setId(
                            Integer.valueOf(getContentResolver().
                                    insert(UsersContract.CONTENT_URI, result).getLastPathSegment()));
                }
                Toast.makeText(TwitterActivity.this, R.string.ok_registration, Toast.LENGTH_SHORT).show();
                cursor.close();
                startActivity(new Intent(TwitterActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    /**
     * check if the account is authorized
     *
     * @return
     */
    private boolean isConnected() {
        return mSharedPreferences.getString(PREF_KEY_TOKEN, null) != null;
    }

    private String getMessage() {
        return mSharedPreferences.getString(EXTRA_MESSAGE_DATA, null);
    }


    private class OAuthUrl extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
                configurationBuilder.setOAuthConsumerKey(CONSUMER_KEY);
                configurationBuilder.setOAuthConsumerSecret(CONSUMER_SECRET);
                twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
                requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("mylog", requestToken.getAuthenticationURL());
            TwitterActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
            finish();
        }
    }
}


