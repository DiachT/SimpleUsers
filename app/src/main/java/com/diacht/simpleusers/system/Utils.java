package com.diacht.simpleusers.system;

import android.database.Cursor;

/**
 * Class helper
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class Utils {
    public static String getStringFromCursor(Cursor cursor, String name) {
        return cursor.getString(cursor.getColumnIndex(name));
    }

    public static int getIntFromCursor(Cursor cursor, String name) {
        return cursor.getInt(cursor.getColumnIndex(name));
    }

    public static long getLongFromCursor(Cursor cursor, String name) {
        return cursor.getLong(cursor.getColumnIndex(name));
    }

    public static double getDoubleFromCursor(Cursor cursor, String name) {
        return cursor.getDouble(cursor.getColumnIndex(name));
    }
}
