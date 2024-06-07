package com.example.mappointer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mappointer.models.Point;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "map_pointer.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "geo_points";
    private static final String COLUMN_GEO_POINT_LATITUDE = "latitude";
    private static final String COLUMN_GEO_POINT_LONGITUDE = "longitude";
    private static final String COLUMN_DESCRIPTION = "description";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_GEO_POINT_LATITUDE + " TEXT PRIMARY KEY, "
                + COLUMN_GEO_POINT_LONGITUDE + " TEXT ,"
                + COLUMN_DESCRIPTION + " TEXT "
                + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertOrUpdatePoints(String latitude, String longitude, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GEO_POINT_LATITUDE, latitude);
        values.put(COLUMN_GEO_POINT_LONGITUDE, longitude);
        values.put(COLUMN_DESCRIPTION, description);

        long id = db.insertWithOnConflict(
                TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        db.close();
    }

    public void insertOrUpdatePoints(Point point, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GEO_POINT_LATITUDE, point.getLatitude());
        values.put(COLUMN_GEO_POINT_LONGITUDE, point.getLongitude());
        values.put(COLUMN_DESCRIPTION, description);

        long id = db.insertWithOnConflict(
                TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        db.close();
    }

    public Cursor getExchangePoint(String latitude, String longitude) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                COLUMN_GEO_POINT_LATITUDE + " = ? and "
                        + COLUMN_GEO_POINT_LONGITUDE + " = ?",
                new String[]{latitude, longitude},
                null,
                null,
                null
        );

        return cursor;
    }

    public List<Point> getAllPoints() {
        List<Point> points = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                true,
                TABLE_NAME,
                new String[]{COLUMN_GEO_POINT_LATITUDE, COLUMN_GEO_POINT_LONGITUDE},
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")String latitude = cursor.getString(cursor.getColumnIndex(COLUMN_GEO_POINT_LATITUDE));
                @SuppressLint("Range")String longitude = cursor.getString(cursor.getColumnIndex(COLUMN_GEO_POINT_LONGITUDE));
                Point point = new Point(latitude, longitude);
                points.add(point);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return points;
    }
}