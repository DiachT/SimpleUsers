package com.diacht.simpleusers.ui.fragment;

import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.adapter.UsersAdapter;
import com.diacht.simpleusers.dao.User;
import com.diacht.simpleusers.db.UsersContract;

/**
 * UsersFragment
 * @author Tetiana Diachuk (diacht@gmail.com) on 18-Aug-14.
 */
public class UsersFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener{
    private UsersAdapter mAdapter;
    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView lv = new ListView(getActivity());
        lv.setPadding(25,15,25,15);
        lv.setOnItemClickListener(this);
        mAdapter = new U
        lv.setAdapter(mAdapter);

        return lv;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.users);
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.content.Loader<Cursor>(getActivity(),
                UsersContract.CONTENT_URI, null,
                User.FIELD_ID + "<> ",
                new String[]{String.valueOf(mSettings.getId())},
                User.FIELD_NAME + " DESC");
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
