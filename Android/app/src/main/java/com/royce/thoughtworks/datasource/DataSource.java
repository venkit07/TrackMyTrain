
package com.royce.thoughtworks.datasource;

/**
 * Created by RRaju on 12/12/2014.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.royce.thoughtworks.database.MySQLiteDataHelper;
import com.royce.thoughtworks.model.Station;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteDataHelper dbHelper;
    private String[] stationAllColumns = {MySQLiteDataHelper.STATTION_NAME,
            MySQLiteDataHelper.STATION_CODE
    };

    public DataSource(Context context) {
        dbHelper = new MySQLiteDataHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Station createStation(String name, String code) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteDataHelper.STATION_CODE, code);
        values.put(MySQLiteDataHelper.STATTION_NAME, name);

        database.insert(MySQLiteDataHelper.TABLE_STATION, null, values);
        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_STATION,
                stationAllColumns, null, null, null, null, null);
        cursor.moveToFirst();
        Station newStation = cursorToStation(cursor);
        cursor.close();
        return newStation;
    }


    public Station getStation(String code) {
        Station Station = new Station();

        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_STATION,
                stationAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Station _Station = cursorToStation(cursor);
            Station = _Station;
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Station;
    }

    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList<Station>();

        Cursor cursor = database.query(MySQLiteDataHelper.TABLE_STATION,
                stationAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Station _Station = cursorToStation(cursor);
            stations.add(_Station);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return stations;
    }


    private Station cursorToStation(Cursor cursor) {
        Station station = new Station();
        station.setName(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.STATTION_NAME)));
        station.setCode(cursor.getString(cursor.getColumnIndex(MySQLiteDataHelper.STATION_CODE)));

        return station;
    }

}