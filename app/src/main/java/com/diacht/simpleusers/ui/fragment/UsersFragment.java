package com.diacht.simpleusers.ui.fragment;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.adapter.UsersAdapter;
import com.diacht.simpleusers.dao.User;
import com.diacht.simpleusers.db.UsersContract;
import com.diacht.simpleusers.ui.view.CursorListItemUsers;

/**
 * UsersFragment
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class UsersFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private Cursor mCursor;
    private ContentObserver mObserver;
    private UsersAdapter mAdapter;
    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mObserver = new ContentObserver(new Handler()) {
            @SuppressWarnings("deprecation")
            @Override
            public void onChange(boolean selfChange) {
                mCursor.requery();
                updateContentFromCursor();
                super.onChange(selfChange);
            }
        };
    }

    private void updateContentFromCursor() {
        if (mCursor == null || !mCursor.moveToFirst()) {
            return;
        }
        mAdapter.changeCursor(mCursor);
        mAdapter.notifyDataSetChanged();
    }

    private void prepareCursor() {
        Cursor cursor = getActivity().getContentResolver().query(
                UsersContract.CONTENT_URI, null, UsersContract._ID + "<> ?",
                new String[]{String.valueOf(mSettings.getId())},
                User.FIELD_NAME + " DESC");
        setCursor(cursor);
    }

    private void setCursor(Cursor cursor) {
        if (cursor == null) {
            if (mCursor != null) {
                mCursor.unregisterContentObserver(mObserver);
                mCursor.close();
                mCursor = null;
            }
        } else {
            mCursor = cursor;
            mCursor.registerContentObserver(mObserver);
            updateContentFromCursor();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView lv = new ListView(getActivity());
        lv.setPadding(25,15,25,15);
        lv.setOnItemClickListener(this);
        mAdapter = new UsersAdapter(getActivity());
        lv.setAdapter(mAdapter);
        prepareCursor();
        return lv;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.users);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!TextUtils.isEmpty(((CursorListItemUsers) view).getUserEmail())) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", ((CursorListItemUsers) view).getUserEmail(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCursor.close();
    }
}
