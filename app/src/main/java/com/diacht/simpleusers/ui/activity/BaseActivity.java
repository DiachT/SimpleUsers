package com.diacht.simpleusers.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.ui.fragment.BaseFragment;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKWallPostResult;

/**
 * Base activity.
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class BaseActivity extends Activity{
    public interface OnSetDialogChoiceListener {
        public void setDialogChoice();
    }

    public static final int REQUEST_VK = VKSdk.VK_SDK_REQUEST_CODE;

    public String mSharingText;
    private BaseFragment mTargetFragment;

    public static final String[] sMyScope = new String[]{
            VKScope.WALL
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
        startFragment(R.id.container, fragment, backStack, fragment.getClass().getSimpleName(), animation);
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
    public void ErrorCoordinatesDialog(int error, final OnSetDialogChoiceListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(error))
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                final int id) {
                                listener.setDialogChoice();
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
                if (mSharingText != null) {
                    makePostVK(null, mSharingText);
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void setSharingVkText(String text){
        mSharingText = text;
    }

    public void makePostVK(VKAttachments attachments, String message) {
        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID,
                VKSdk.getAccessToken().userId,
                VKApiConst.ATTACHMENTS, attachments, VKApiConst.MESSAGE, message));
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Toast.makeText(BaseActivity.this, "add", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://vk.com/wall"
//                        + VKSdk.getAccessToken().userId + "_%s", ((VKWallPostResult) response.parsedModel).post_id)) );
//                startActivity(i);
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(BaseActivity.this, "error", Toast.LENGTH_SHORT).show();
                showError(error.apiError != null ? error.apiError : error);
            }
        });
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
        public void onCaptchaError(VKError captchaError) {
//            new VKCaptchaDialog(captchaError).show();
        }

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
        public void onReceiveNewToken(VKAccessToken newToken) {
            /*startTestActivity();*/
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
//            startTestActivity();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof BaseFragment) {
            mTargetFragment = (BaseFragment) fragment;
        }
    }
}
