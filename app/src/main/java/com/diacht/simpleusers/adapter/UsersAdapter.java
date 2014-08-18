package com.diacht.simpleusers.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.diacht.simpleusers.ui.view.CursorListItemUsers;

/**
 * UsersAdapter
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class UsersAdapter extends CursorAdapter{
    public UsersAdapter(Context context) {
        super(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public void bindView(View arg0, Context arg1, Cursor arg2) {
        ((CursorListItemUsers)arg0).setData(arg2);
    }

    @Override
    public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
        CursorListItemUsers view = new CursorListItemUsers(arg0, null);
        view.setData(arg1);
        return view;
    }
}

