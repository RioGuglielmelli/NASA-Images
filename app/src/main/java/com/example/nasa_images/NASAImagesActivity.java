package com.example.nasa_images;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NASAImagesActivity() {

    //connect to database:
    Opener dbOpener = new Opener(this);
    SQLiteDatabase db = dbOpener.getWritableDatabase();
    //calls to call columns listed
    String[] columns ={Opener.COL_ID,Opener.COL_TITLE, Opener.COL_EXPLANATION, Opener.COL_URL, Opener.COL_HDURL, Opener.COL_DATE);
    //query all results from database:
    Cursor results = db.query(false, Opener.TABLE_NAME, columns, null, null, null, null, null, null);
    printCursor(results);}
