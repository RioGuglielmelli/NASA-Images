package com.example.nasa_images;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NASAImagesActivity() extends BaseActivity {
    private ArrayList<NASAImages> elements = new ArrayList<>();
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_log);
        Button l4B = (Button) findViewById(R.id.buttonL4);
        EditText l4ET = (EditText) findViewById(R.id.editTextL4);
        Button deB = (Button) findViewById(R.id.deleteButton);
        ListView l4List = findViewById(R.id.listViewL4);
        myListAdapter myAdapter;
        l4List.setAdapter(myAdapter = new myListAdapter());
        loadDataDB();

    //connect to database:
    Opener dbOpener = new Opener(this);
    SQLiteDatabase db = dbOpener.getWritableDatabase();
    //calls to call columns listed
    String[] columns ={Opener.COL_ID,Opener.COL_TITLE, Opener.COL_EXPLANATION, Opener.COL_URL, Opener.COL_HDURL, Opener.COL_DATE);
    //query all results from database:
    Cursor results = db.query(false, Opener.TABLE_NAME, columns, null, null, null, null, null, null);
    printCursor(results);

    results = db.query(false, Opener.TABLE_NAME, columns, null, null, null, null, null, null);
    int titleColIndex = results.getColumnIndex(Opener.COL_TITLE);
    int explanationColIndex = results.getColumnIndex(Opener.COL_EXPLANATION);
    int urlColIndex = results.getColumnIndex(Opener.COL_URL);
    int hdurlColIndex = results.getColumnIndex(Opener.COL_HDURL);
    int DATEColIndex = results.getColumnIndex(Opener.COL_DATE);
    int idColIndex = results.getColumnIndex(Opener.COL_ID);



        //delete button in image log
        Button deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get a reference to the database
                SQLiteDatabase db = dbOpener.getWritableDatabase();

                // Define the table and column to delete from
                String table = Opener.TABLE_NAME;
                String whereClause = Opener.COL_ID + " = ?";

                // Define the value for the WHERE clause
                String[] whereArgs = new String[] { idToDelete };

                // Delete the row from the database
                db.delete(table, whereClause, whereArgs);

                // Close the database connection
                db.close();
            }
        });

    }
    }
}
