package com.diacht.simpleusers.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.dao.User;
import com.diacht.simpleusers.db.UsersContract;
import com.diacht.simpleusers.system.SUApplication;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;

/**
 * Base activity.
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class BaseActivity extends Activity{
    public interface OnSetDialogChoiceListener {
        public void setDialogChoice(boolean addCoordinates);
    }

    public static final int REQUEST_VK = VKSdk.VK_SDK_REQUEST_CODE;

    public static final String[] sMyScope = new String[]{
//            VKScope.WALL
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startFragment(int resId, Fragment fragment,
                              boolean addToStack, String stackTag, boolean animation) {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (animation)
            ft.setCustomAnimations(R.anim.slidein_right, R.anim.slideout_left,
                    R.anim.slidein_left, R.anim.slideout_right);
        ft.replace(resId, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToStack) {
            ft.addToBackStack(stackTag);
        }
        ft.commit();
    }

    public void startFragment(Fragment fragment, boolean backStack, boolean animation) {
        startFragment(R.id.container, fragment, backStack,
                fragment.getClass().getSimpleName(), animation);
    }

    public void startFragment(Fragment fragment, boolean backStack) {
        startFragment(R.id.container, fragment, backStack, fragment.getClass().getSimpleName(), true);
    }

    /**
     * Выдает диалог об ошибках
     */
    public void ErrorDialog(int error) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(error))
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                final int id) {
                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Выдает диалог о незаданных координатах
     */
    public void ErrorCoordinatesDialog(int error, final OnSetDialogChoiceListener listener,
                                       final boolean addCoordinates) {
        ErrorCoordinatesDialog(getResources().getString(error), listener, addCoordinates);
    }

    public void ErrorCoordinatesDialog(String error, final OnSetDialogChoiceListener listener, final boolean addCoordinates) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                final int id) {
                                listener.setDialogChoice(addCoordinates);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                final int id) {
                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_VK) {
            VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
            if (VKSdk.wakeUpSession()) {
                int id = Integer.parseInt(VKSdk.getAccessToken().userId);
                Cursor cursor = getContentResolver().query(UsersContract.CONTENT_URI, null,
                        UsersContract._ID + "=? ",
                        new String[]{String.valueOf(id)},
                        null);
                if(!cursor.moveToFirst()){
                    ContentValues result = new ContentValues();
                    result.put(UsersContract._ID, id);
//                    result.put(UsersContract.login, id);
                    result.put(UsersContract.password, "");
                    result.put(UsersContract.longitude, User.NO_COORDINATES);
                    result.put(UsersContract.latitude, User.NO_COORDINATES);
                    ((SUApplication)getApplication()).getSettings().setId(
                            Integer.valueOf(getContentResolver().
                                    insert(UsersContract.CONTENT_URI, result).getLastPathSegment()));
                }
                Toast.makeText(this, R.string.ok_registration, Toast.LENGTH_SHORT).show();
                cursor.close();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showError(VKError error) {
        new AlertDialog.Builder(this)
                .setMessage(error.errorMessage)
                .setPositiveButton("OK", null)
                .show();

        if (error.httpError != null) {
            Log.w("Test", "Error in request or upload", error.httpError);
        }
    }

    public final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {}

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(sMyScope);
        }

        @Override
        public void onAccessDenied(final VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {}

        @Override
        public void onAcceptUserToken(VKAccessToken token) {}
    };

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    /**
     * disconnectTwitter
     * Remove Token, Secret from preferences
     */
    public void disconnectTwitter() {
        SharedPreferences sharedPreferences = getSharedPreferences(TwitterActivity.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TwitterActivity.PREF_KEY_TOKEN);
        editor.remove(TwitterActivity.PREF_KEY_SECRET);
        editor.commit();
    }
}
