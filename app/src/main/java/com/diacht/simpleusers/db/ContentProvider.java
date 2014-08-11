package com.diacht.simpleusers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tjeannin.provigen.ProviGenProvider;
import com.tjeannin.provigen.helper.TableBuilder;
import com.tjeannin.provigen.helper.TableUpdater;
import com.tjeannin.provigen.model.Constraint;

/**
 * ContentProvider
 * @author Tetiana Diachuk (diacht@gmail.com)
 */

public class ContentProvider extends ProviGenProvider {
    private static final int DB_VERSION = 6;
    private static Class[] contracts = new Class[]{UsersContract.class};

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new SQLiteOpenHelper(getContext(), "dbName", null, DB_VERSION) {

            @Override
            public void onCreate(SQLiteDatabase database) {
                // Automatically creates table and needed columns.
                new TableBuilder(UsersContract.class)
                        .addConstraint(UsersContract.innerId, Constraint.UNIQUE,
                                Constraint.OnConflict.REPLACE).createTable(database);
            }

            @Override
            public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
                TableUpdater.addMissingColumns(database, UsersContract.class);
            }
        };
    }

    @Override
    public Class[] contractClasses() {
        return contracts;
    }
}
