package com.example.nasa_images;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**

 A SQLiteOpenHelper class for creating and upgrading a database of NASAImages.
 This class contains constants for the database name, version number, and column names. It also defines functions for
 creating, upgrading, and downgrading the database when the version number is changed. The onCreate() function is
 called when the database is created for the first time, and it creates a table with the specified column names. The
 onUpgrade() and onDowngrade() functions are called when the database version number changes, and they drop the old
 table and create a new one with the updated column names.
 */

public class Opener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "NASAImagesDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "NASAIMAGESLIST";
    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_EXPLANATION = "EXPLANATION";
    public final static String COL_URL = "URL";
    public final static String COL_HDURL = "HDURL";
    public final static String COL_DATE = "DATE";


    public Opener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    // function gets called if no database file exists

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITLE + " text," + COL_EXPLANATION + " text," + COL_URL + " text,"+ COL_HDURL + " text," + COL_DATE + " text UNIQUE);");
    }
    // function gets called if database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    // function gets called if database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}