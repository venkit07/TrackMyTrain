
package com.royce.thoughtworks.database;

/**
 * Created by RRaju on 12/11/2014.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteDataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "thoughtworks.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_STATION = "station";
    public static final String TABLE_ALARM = "alarms";

    public static final String STATTION_NAME = "name";
    public static final String STATION_CODE = "code";

    // Database creation sql statement
    private static final String STATION_CREATE = "create table " + TABLE_STATION
            + "(" + STATION_CODE
            + " text primary key, "
            + STATTION_NAME
            + " text not null);";

    public MySQLiteDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(STATION_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteDataHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        onCreate(db);
    }

}
