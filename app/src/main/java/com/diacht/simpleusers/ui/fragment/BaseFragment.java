package com.diacht.simpleusers.ui.fragment;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.diacht.simpleusers.ui.activity.BaseActivity;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;

/**
 * BaseFragment
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class BaseFragment extends Fragment {
    public ActionBar mActionBar;

    protected void startFragment(Fragment fragment, boolean backStack) {
        ((BaseActivity)getActivity()).startFragment(fragment, backStack);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getActivity().getActionBar();
        setHasOptionsMenu(true);
    }

    protected void actionVk(){
//        VKUIHelper.onCreate(getActivity());
//        VKSdk.initialize(((BaseActivity) getActivity()).sdkListener, "4420954");
//        if (!VKSdk.isLoggedIn()) {
//            ((BaseActivity)getActivity()).setSharingVkText(getSharingText());
//            VKSdk.authorize(BaseActivity.sMyScope, true, false);
////                VKSdk.logout();
//        }else {
//            if (VKSdk.wakeUpSession()) {
//                ((BaseActivity)getActivity()).makePostVK(null, getSharingText());
//            }
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(getActivity(), TwitterActivity.class));
//            return true;
//        }
//        if (id == R.id.action_vk) {
//            actionVk();
//            return true;
//        }
             return false;
    }
}
